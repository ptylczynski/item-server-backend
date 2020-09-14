package cloud.ptl.itemserver.persistence.dao.bundle;

import cloud.ptl.itemserver.persistence.dao.authorization.AclIdentityDAO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

// TODO nullable = false set for all entities

@Data
@Entity(name = "bundle")
public class BundleDAO implements LongIndexed, WithSecurityIdentity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;

    @OneToOne
    private AclIdentityDAO securityIdentityDAO;
}
