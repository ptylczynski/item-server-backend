package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclEntryDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclIdentityDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.repositories.authorization.AclIdentityRepository;
import cloud.ptl.itemserver.persistence.repositories.authorization.AclEntryRepository;
import cloud.ptl.itemserver.service.abstract2.AbstractSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Component
public class SecurityService extends AbstractSecurityService {

    @Autowired
    private AclEntryRepository aclRepository;

    @Autowired
    private AclIdentityRepository aclIdentityRepository;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(SecurityService.class);

    @Override
    public Boolean hasPermission(LongIndexed object, AclPermission permission) {
        UserDAO userDAO = this.userService.getLoggedInUserDAO();
        AclIdentityDAO aclIdentityDAO = this.fetchAclIdentity(object);
        AclEntryDAO aclEntryDAO = this.fetchAclEntryDAO(
                aclIdentityDAO,
                userDAO
        );
        this.logger.info("Checking if user has valid permissions");
        this.logger.debug("user: " + userDAO.toString());
        this.logger.debug("aclIdentity: " + aclIdentityDAO.toString());
        this.logger.debug("aclEntry: " + aclEntryDAO.toString());
        if(aclEntryDAO == null) return false;
        else {
            return aclEntryDAO.getAclPermissions().stream()
                    .anyMatch(permission::equals);
        }
    }

    @Override
    public Boolean hasPermissions(LongIndexed object, List<AclPermission> permissions) {
        UserDAO userDAO = (UserDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AclIdentityDAO aclIdentityDAO = this.fetchAclIdentity(object);
        AclEntryDAO aclEntryDAO = this.fetchAclEntryDAO(
                aclIdentityDAO,
                userDAO
        );
        this.logger.info("Checking if user has valid permissions");
        this.logger.debug("user: " + userDAO.toString());
        this.logger.debug("aclIdentity: " + aclIdentityDAO.toString());
        this.logger.debug("aclEntry: " + aclEntryDAO.toString());
        if(aclEntryDAO == null) return false;
        else {
            return aclEntryDAO.getAclPermissions().stream()
                    .allMatch(a -> permissions.stream()
                            .anyMatch(b -> b.equals(a))
                    );
        }
    }

    @Override
    public void grantPermission(LongIndexed object, AclPermission permission, UserDAO user) {
        AclIdentityDAO aclIdentityDAO = this.fetchAclIdentity(object);
        AclEntryDAO aclEntryDAO = this.fetchAclEntryDAO(
                aclIdentityDAO,
                user
        );
        this.logger.info("Granting user new permission");
        this.logger.debug("user: " + user.toString());
        this.logger.debug("aclEntry: " + aclEntryDAO.toString());
        aclEntryDAO.getAclPermissions().add(permission);
        this.aclRepository.save(aclEntryDAO);
    }

    @Override
    public void grantPermission(LongIndexed object, AclPermission permission) {
        UserDAO userDAO = this.userService.getLoggedInUserDAO();
        this.grantPermission(object, permission, userDAO);
    }

    @Override
    public void revokePermission(LongIndexed object, AclPermission permission, UserDAO user) {
        AclIdentityDAO aclIdentityDAO = this.fetchAclIdentity(object);
        AclEntryDAO aclEntryDAO = this.fetchAclEntryDAO(
                aclIdentityDAO,
                user
        );

        this.logger.info("revoking user new permission");
        this.logger.debug("user: " + user.toString());
        this.logger.debug("aclEntry: " + aclEntryDAO.toString());
        aclEntryDAO.getAclPermissions().remove(permission);
        this.aclRepository.save(aclEntryDAO);
    }

    @Override
    public void revokePermission(LongIndexed object, AclPermission permission) {
        UserDAO userDAO = this.userService.getLoggedInUserDAO();
        this.revokePermission(object, permission, userDAO);
    }

    private AclIdentityDAO fetchAclIdentity(LongIndexed object) {
        // TODO throw access denied
        this.logger.info("Fetching acl identity from database");
        Optional<AclIdentityDAO> aclIdentityDAO =
                this.aclIdentityRepository.findAclIdentityDAOByClazzAndObjectId(
                        object.getClass().getCanonicalName(),
                        object.getId()
                );
        if(aclIdentityDAO.isEmpty())
            return this.createAclIdentity(object);
        else
            return aclIdentityDAO.get();
    }

    private AclIdentityDAO createAclIdentity(LongIndexed object){
        this.logger.info("acl identity not found - creating new");
        return this.aclIdentityRepository.save(
                AclIdentityDAO.builder()
                        .clazz(object.getClass().getCanonicalName())
                        .id(object.getId())
                        .build()
        );
    }

    private AclEntryDAO fetchAclEntryDAO(AclIdentityDAO aclIdentityDAO, UserDAO userDAO) {
        // TODO throw access denied
        this.logger.info("Fetching acl entry from db");
        Optional<AclEntryDAO> aclEntryDAO =
                this.aclRepository.findAclEntryDAOByAclIdentityDAOAndUserDAO(
                        aclIdentityDAO,
                        userDAO
                );
        if(aclEntryDAO.isEmpty())
            return this.createAclEntryDAO(aclIdentityDAO, userDAO);
        else
            return aclEntryDAO.get();
    }

    private AclEntryDAO createAclEntryDAO(AclIdentityDAO aclIdentityDAO, UserDAO userDAO){
        this.logger.info("acl entry not found - creating new");
        return this.aclRepository.save(
                AclEntryDAO.builder()
                    .aclIdentityDAO(aclIdentityDAO)
                    .userDAO(userDAO)
                        .aclPermissions(new HashSet<>())
                    .build()
        );
    }
}
