package cloud.ptl.itemserver.security;

import cloud.ptl.itemserver.persistence.dao.authorization.CompoundPermission;
import cloud.ptl.itemserver.persistence.dao.authorization.Permission;
import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;
import cloud.ptl.itemserver.persistence.helper.service.SecurityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private SecurityService securityService;

    @SneakyThrows
    @Override
    public boolean hasPermission(
            Authentication authentication,
            Object domainObject,
            Object permissionString) {

        // cast domain object
        if(Arrays.stream(domainObject.getClass().getInterfaces()).noneMatch(
                a -> a.equals(WithSecurityIdentity.class)
        )) throw new IllegalArgumentException(
                "Domain object does not have With Security Identity interface implemented");

        // cast permission
        try{
            return this.securityService.hasAccess(
                    (WithSecurityIdentity) domainObject,
                    CompoundPermission.valueOf(((String) permissionString).toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            try {
                return this.securityService.hasAccess(
                        (WithSecurityIdentity) domainObject,
                        Permission.valueOf(((String) permissionString).toUpperCase())
                );
            }
            catch (IllegalArgumentException ex){
                throw new IllegalArgumentException(
                        "Permission is nor Permission nor CompoundPermission class"
                );
            }
        }
    }

    @SneakyThrows
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new Exception(
                "ACL authorization via class name and ID is not and will not be supported." +
                        "To use ACL provide object and permission instead"
        );
    }
}
