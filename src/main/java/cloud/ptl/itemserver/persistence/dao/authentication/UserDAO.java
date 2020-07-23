package cloud.ptl.itemserver.persistence.dao.authentication;

import cloud.ptl.itemserver.persistence.dao.AbstractDAO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "user")
@Table(name = "user")
@Data
@Builder
public class UserDAO extends AbstractDAO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
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

    private UserDetails toUser(){
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
