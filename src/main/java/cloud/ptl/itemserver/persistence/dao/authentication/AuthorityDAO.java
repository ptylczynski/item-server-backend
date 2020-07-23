package cloud.ptl.itemserver.persistence.dao.authentication;

import cloud.ptl.itemserver.persistence.dao.AbstractDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "authorities")
public class AuthorityDAO implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;
}
