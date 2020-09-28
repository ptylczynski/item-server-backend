package cloud.ptl.itemserver.error.exception.permission;

import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import lombok.*;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.BAD_REQUEST,
        reason = "user has insufficient permissions"
)
public class InsufficientPermission extends AccessDeniedException {

    @Setter
    @Getter
    private String clazz;

    @Getter
    @Setter
    private AclPermission requiredPermission;

    @Getter
    @Setter
    private Link link;

    public InsufficientPermission(String clazz, AclPermission aclPermission, Link link) {
        super("");
        this.setClazz(clazz);
        this.setRequiredPermission(aclPermission);
        this.setLink(link);
    }
}
