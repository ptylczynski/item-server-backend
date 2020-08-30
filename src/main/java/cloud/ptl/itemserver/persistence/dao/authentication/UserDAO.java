package cloud.ptl.itemserver.persistence.dao.authentication;

import cloud.ptl.itemserver.persistence.dao.AbstractDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(exclude = "locatorOf")
@Entity(name = "user")
@Table(name = "user")
@Data
public class UserDAO extends AbstractDAO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String displayName;
    private String password;
    private String mail;
    private Boolean isEnabled;
    private Boolean accountNotExpired;
    private Boolean credentialsNotExpired;
    private Boolean accountNotLocked;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(name = "user_fk"),
            inverseJoinColumns = @JoinColumn(name = "authority_fk")
    )
    private List<AuthorityDAO> authorityDAOList;

    @ManyToOne
    private BundleDAO locatorOf;

    public User toUser(){
        return new User(
                this.username,
                this.password,
                this.isEnabled,
                this.accountNotExpired,
                this.credentialsNotExpired,
                this.accountNotLocked,
                this.authorityDAOList
        );
    }
}
