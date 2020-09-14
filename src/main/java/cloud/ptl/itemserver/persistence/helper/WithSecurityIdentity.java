package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.persistence.dao.authorization.AclIdentityDAO;

public interface WithSecurityIdentity {
    AclIdentityDAO getSecurityIdentityDAO();
    void setSecurityIdentityDAO(AclIdentityDAO hash);
}
