package cloud.ptl.itemserver.persistence.dao.bundle;

import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

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
    private String securityHash;

    @OneToOne
    private UserDAO owner;

    @ManyToMany
    @JoinTable(
            name = "bundle_viewer",
            joinColumns = @JoinColumn(name = "bundle_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserDAO> viewers;

    @ManyToMany
    @JoinTable(
            name = "bundle_editor",
            joinColumns = @JoinColumn(name = "bundle_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserDAO> editors;
}
