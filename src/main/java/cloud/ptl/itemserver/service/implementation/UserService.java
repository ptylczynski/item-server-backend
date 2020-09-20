package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.controllers.UserController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ObjectNotFound objectNotFound = new ObjectNotFound(
            UserDAO.class.getCanonicalName(),
            WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
    );

    public Boolean checkIfUserExist(Long id) throws ObjectNotFound {
        this.logger.info("Checking if user exists");
        if(!this.userRepository.existsById(id)){
            this.logger.debug("User does not exist");
            throw this.objectNotFound;
        }
        this.logger.debug("User exists");
        return true;
    }

    // used for mail or user name check
    public Boolean checkIfUserExist(String designator) throws ObjectNotFound {
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
        this.checkIfUserExist(id);
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

    public List<UserDAO> findAll(Pageable pageable){
        return this.userRepository.findAll(pageable).getContent();
    }

    public Boolean hasAccess(UserDAO userDAO, AclPermission permission){
        if(this.securityService.hasPermission(userDAO, permission)) return true;
        throw new AccessDeniedException("Access Denied");
    }

    public UserDAO findByUsername(String username) throws ObjectNotFound {
        Optional<UserDAO> userDAO = this.userRepository.findByUsername(username);
        if(userDAO.isEmpty())
            throw new ObjectNotFound(
                    UserDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        return userDAO.get();
    }

    public UserDAO findByMail(String mail) throws ObjectNotFound {
        Optional<UserDAO> userDAO =
                this.userRepository.findByMail(mail);
        if(userDAO.isEmpty())
            throw new ObjectNotFound(
                    UserDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        return userDAO.get();
    }
}
