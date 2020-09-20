package cloud.ptl.itemserver.service.abstract2;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;

public abstract class AbstractSecurityService {
    // public abstract Object hasAccess(Object object, AclPermission permission);
    // public abstract List<Object> hasAccess(List<Object> objects, AclPermission permission);
    public abstract Boolean hasPermission(LongIndexed object, AclPermission permissions);
    public abstract void grantPermission(LongIndexed object, AclPermission permission, UserDAO user);
    public abstract void grantPermission(LongIndexed object, AclPermission permission);
    public abstract void revokePermission(LongIndexed object, AclPermission permission, UserDAO user);
    public abstract void revokePermission(LongIndexed object, AclPermission permission);
}
