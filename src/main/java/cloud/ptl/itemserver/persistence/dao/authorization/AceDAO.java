package cloud.ptl.itemserver.persistence.dao.authorization;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ace")
@Data
public class AceDAO implements LongIndexed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String securityHash;

    @OneToOne
    @JoinColumn(name = "user_fk")
    private UserDAO userDAO;

    @ElementCollection(targetClass = Permission.class)
    @CollectionTable(
            name = "ace_permission",
            joinColumns = @JoinColumn(name = "ace_fk")
    )
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;
}
