package cloud.ptl.itemserver.persistence.dao.authorization;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "acl")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AclEntryDAO {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "acl_identity_fk")
    private AclIdentityDAO aclIdentityDAO;

    @OneToOne
    @JoinColumn(name = "user_fk")
    private UserDAO userDAO;

    @ElementCollection(targetClass = AclPermission.class)
    @CollectionTable(
            name = "acl_entry_permission",
            joinColumns = @JoinColumn(name = "acl_entry_fk")
    )
    @Enumerated(EnumType.STRING)
    private Set<AclPermission> aclPermissions;
}
