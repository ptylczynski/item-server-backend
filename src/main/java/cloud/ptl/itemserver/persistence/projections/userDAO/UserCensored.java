package cloud.ptl.itemserver.persistence.projections.userDAO;

import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;

public interface UserCensored extends WithSecurityIdentity {
    Long getId();
    String getUsername();
    String getDisplayName();
    String getMail();
}
