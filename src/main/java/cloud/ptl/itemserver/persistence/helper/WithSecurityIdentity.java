package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.persistence.dao.authorization.SecurityIdentityDAO;

public interface WithSecurityIdentity {
    SecurityIdentityDAO getSecurityIdentityDAO();
    void setSecurityIdentityDAO(SecurityIdentityDAO hash);
}
