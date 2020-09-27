package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.controllers.UserController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.permission.InsufficientPermission;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import cloud.ptl.itemserver.service.abstract2.AbstractDAOService;
import cloud.ptl.itemserver.service.abstract2.AbstractMailingService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService extends AbstractDAOService<UserDAO> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailingService mailingService;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ObjectNotFound objectNotFound = new ObjectNotFound(
            UserDAO.class.getCanonicalName(),
            WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
    );

    public Boolean checkIfExists(Long id) throws ObjectNotFound {
        this.logger.info("Checking if user exists");
        if(!this.userRepository.existsById(id)){
            this.logger.debug("User does not exist");
            throw this.objectNotFound;
        }
        this.logger.debug("User exists");
        return true;
    }

    // used for mail or user name check
    public Boolean checkIfExists(String designator) throws ObjectNotFound {
        this.logger.info("Checking if user exist");
        this.logger.debug("Designator=" + designator);
        if(designator.contains("@")){
            this.logger.debug("Designator is mail");
            // designator is mail address
            if(!this.userRepository.existsByMail(designator)){
                this.logger.debug("User not found");
                throw this.objectNotFound;
            }
        }
        else{
            // designator is username
            this.logger.debug("Designator is username");
            if(!this.userRepository.existsByUsername(designator)){
                this.logger.debug("User not found");
                throw this.objectNotFound;
            }
        }
        this.logger.debug("User found");
        return true;
    }

    public UserDAO findById(Long id) throws ObjectNotFound {
        this.logger.info("Searching user");
        this.logger.debug("id=" + id);
        this.checkIfExists(id);
        return this.userRepository.findById(id).get();
    }

    public UserDAO getLoggedInUserDAO(){
        UserDAO userDAO =
                (UserDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.logger.info("Checking logged in user");
        this.logger.debug("user: " + userDAO.toString());
        return userDAO;
    }

//    public List<UserDAO> findAll(Pageable pageable){
//        List<AclEntryDAO> aclEntryDAOS =
//                this.securityService.getAllAccesiableAclEntries(
//                        UserDAO.class,
//                        pageable
//                );
//        return this.userRepository.findAllById(
//                aclEntryDAOS.stream()
//                        .map(a -> a.getAclIdentityDAO().getId())
//                        .collect(Collectors.toSet())
//        );
//    }

    public List<UserDAO> findAll(Pageable pageable, AclPermission permission){
        this.logger.info("Fetching all users");
        return this.userRepository.findAll(pageable).getContent();
    }

    public Boolean hasAccess(UserDAO userDAO, AclPermission permission){
        this.logger.info("Checking if user has access");
        if(this.securityService.hasPermission(userDAO, permission)) return true;
        throw new InsufficientPermission(
                UserDAO.class.getCanonicalName(),
                permission,
                WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
        );
    }

    public UserDAO findByUsername(String username) throws ObjectNotFound {
        this.logger.info("Searching user by username");
        Optional<UserDAO> userDAO = this.userRepository.findByUsername(username);
        if(userDAO.isEmpty())
            throw new ObjectNotFound(
                    UserDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        return userDAO.get();
    }

    public UserDAO findByMail(String mail) throws ObjectNotFound {
        this.logger.info("Searching user by mail");
        Optional<UserDAO> userDAO =
                this.userRepository.findByMail(mail);
        if(userDAO.isEmpty())
            throw new ObjectNotFound(
                    UserDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        return userDAO.get();
    }

    public void registerUser(UserDAO userDAO) throws MessagingException, UnsupportedEncodingException {
        this.logger.debug("encoding password");
        userDAO.setPassword(
                this.passwordEncoder.encode(
                        userDAO.getPassword()
                )
        );
        userDAO.setAccountNonExpired(true);
        userDAO.setAccountNonLocked(true);
        userDAO.setCredentialsNonExpired(true);
        userDAO.setEnabled(false);
        userDAO = this.userRepository.save(userDAO);
        this.securityService.grantPermission(userDAO, AclPermission.EDITOR, userDAO);
        Map<String, Object> properties = new HashMap<>();
        properties.put("user_id", userDAO.getId());
        properties.put("username", userDAO.getUsername());
        properties.put(
                "code",
                this.createCode(userDAO)
        );
        this.mailingService.sendMail(
                AbstractMailingService.MailType.ACTIVATE_ACC,
                userDAO,
                properties
        );
    }

    public void updateUser(UserDAO userDAO) throws ObjectNotFound {
        UserDAO currentlyLoggedInUser =
                this.getLoggedInUserDAO();
        if(!currentlyLoggedInUser.equals(userDAO)){
            this.logger.debug("User to edit is not the same as logged in user");
            throw new InsufficientPermission(
                    UserDAO.class.getCanonicalName(),
                    AclPermission.EDITOR,
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        }
        this.checkIfExists(userDAO.getId());
        // encrypt new password
        userDAO.setPassword(
                this.passwordEncoder.encode(
                        userDAO.getPassword()
                )
        );
        // block possibility to enable account just by PUT modified account
        userDAO.setEnabled(
                currentlyLoggedInUser.isEnabled()
        );
        userDAO.setCredentialsNonExpired(
                currentlyLoggedInUser.isCredentialsNonExpired()
        );
        userDAO.setAccountNonLocked(
                currentlyLoggedInUser.isAccountNonLocked()
        );
        userDAO.setAccountNonExpired(
                currentlyLoggedInUser.isAccountNonExpired()
        );
        this.userRepository.save(userDAO);
    }

    private String createCode(UserDAO userDAO){
        this.logger.info("Creating code for userDAO");
        // this.logger.debug("user: " + userDAO.toString());
        String code = DigestUtils.sha1Hex(userDAO.getUsername() + userDAO.getPassword()).substring(20);
        this.logger.debug("Code: " + code);
        return code;
    }

    public Boolean validateCode(UserDAO userDAO, String code){
        this.logger.info("Validating user code");
        Boolean isCodeValid = this.createCode(userDAO).equals(code);
        this.logger.debug("is code valid: " + isCodeValid);
        return isCodeValid;
    }
}
