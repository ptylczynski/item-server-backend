package cloud.ptl.itemserver.persistence.helper.service;

import cloud.ptl.itemserver.controllers.UserController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AceDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.CompoundPermission;
import cloud.ptl.itemserver.persistence.dao.authorization.Permission;
import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;
import cloud.ptl.itemserver.persistence.repositories.authorization.AceRepository;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SecurityService {

    @Autowired
    private AceRepository aceRepository;

    @Autowired
    private UserRepository userRepository;

    public Boolean hasAccess(WithSecurityIdentity object, Permission permission) throws ObjectNotFound {
        return this.fetchAceDAO(object).getPermissions()
                .stream()
                .anyMatch(
                    a -> a == permission
                );
    }

    public Boolean hasAccess(WithSecurityIdentity object, CompoundPermission compoundPermission) throws ObjectNotFound {
        // check if all permissions in compound permission
        // are in ace permission list
        Set<Permission> permissions = this.fetchAceDAO(object).getPermissions();
        return Arrays.stream(compoundPermission.getPermissions())
                .allMatch(
                        compound -> permissions.stream().anyMatch(
                                compound::equals
                        )
                );
    }

    public void grantPermission(WithSecurityIdentity object, Permission permission, UserDAO userDAO) {
        AceDAO aceDAO = this.fetchAceDAO(object, userDAO);
        aceDAO.getPermissions().add(permission);
        this.aceRepository.save(aceDAO);
    }

    public void grantPermission(WithSecurityIdentity object, CompoundPermission compoundPermission, UserDAO userDAO) {
        AceDAO aceDAO = this.fetchAceDAO(object, userDAO);
        aceDAO.getPermissions().addAll(
                List.of(compoundPermission.getPermissions())
        );
        this.aceRepository.save(aceDAO);
    }

    public void revokePermission(WithSecurityIdentity object, Permission permission, UserDAO userDAO) {
        AceDAO aceDAO = this.fetchAceDAO(object, userDAO);
        aceDAO.getPermissions().remove(permission);
        this.aceRepository.save(aceDAO);
    }

    public void revokePermission(WithSecurityIdentity object, CompoundPermission compoundPermission, UserDAO userDAO) {
        AceDAO aceDAO = this.fetchAceDAO(object, userDAO);
        aceDAO.getPermissions().removeAll(
                List.of(compoundPermission.getPermissions())
        );
        this.aceRepository.save(aceDAO);
    }

    public AceDAO fetchAceDAO(WithSecurityIdentity object, UserDAO userDAO){
        String hash = object.getSecurityHash();
        Optional<AceDAO> optionalAceDAO = this.aceRepository.findBySecurityHashAndUserDAO(hash, userDAO);
        AceDAO aceDAO;
        if(optionalAceDAO.isEmpty()){
            aceDAO = new AceDAO();
            aceDAO.setUserDAO(userDAO);
            aceDAO.setSecurityHash(hash);
            aceDAO.setPermissions(new HashSet<>());
        }
        else aceDAO = optionalAceDAO.get();
        return aceDAO;
    }

    public AceDAO fetchAceDAO(WithSecurityIdentity object) throws ObjectNotFound {
        return this.fetchAceDAO(
                object,
                this.getUserAsUserDAO()
        );
    }

    public UserDAO getUserAsUserDAO() throws ObjectNotFound {
        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserDAO> userDAO = this.userRepository.findByUsername(username);
        if(userDAO.isEmpty()){
            throw new ObjectNotFound(
                    UserDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        }
        return userDAO.get();
    }

    public void setSecurityHash(WithSecurityIdentity object){
        object.setSecurityHash(
                DigestUtils.sha512Hex(object.toString())
        );
    }
}
