package cloud.ptl.itemserver.security;

import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;
import cloud.ptl.itemserver.service.implementation.SecurityService;
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

        // check if object implements Long Indexed
        if(Arrays.stream(domainObject.getClass().getInterfaces()).noneMatch(
                a -> a.equals(LongIndexed.class)
        )) throw new IllegalArgumentException(
                "Domain object does not have With Security Identity interface implemented");

        AclPermission permission = AclPermission.valueOf(
                ((String) permissionString).toUpperCase()
        );
        LongIndexed object = (LongIndexed) domainObject;
        return this.securityService.hasPermission(object, permission);
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
