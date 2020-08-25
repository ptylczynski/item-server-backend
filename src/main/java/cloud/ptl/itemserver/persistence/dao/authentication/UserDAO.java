package cloud.ptl.itemserver.persistence.dao.authentication;

import cloud.ptl.itemserver.persistence.dao.AbstractDAO;
import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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
    private AddressDAO locatorOf;

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
