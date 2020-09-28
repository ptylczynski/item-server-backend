package cloud.ptl.itemserver.persistence.dao.authentication;

import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.validators.annotation.NotDuplicated;
import cloud.ptl.itemserver.persistence.validators.implementations.NotDuplicatedValidator;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Table(name = "user")
@Data
@Entity
public class UserDAO implements LongIndexed, UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty String displayName;
    @NotEmpty String password;

    @NotEmpty
    @NotDuplicated(value = NotDuplicatedValidator.Entity.USERNAME)
    String username;

    @NotEmpty
    @NotDuplicated(value = NotDuplicatedValidator.Entity.EMAIL)
    String mail;

    boolean enabled;
    boolean accountNonExpired;
    boolean credentialsNonExpired;
    boolean accountNonLocked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(name = "user_fk"),
            inverseJoinColumns = @JoinColumn(name = "authority_fk")
    )
    List<AuthorityDAO> authorities;
}
