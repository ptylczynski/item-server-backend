package cloud.ptl.itemserver.persistence.projections.userDAO;

public interface UserCensored {
    Long getId();
    String getUsername();
    String getDisplayName();
    String getMail();
}
