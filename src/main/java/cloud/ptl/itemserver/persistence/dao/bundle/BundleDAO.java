package cloud.ptl.itemserver.persistence.dao.bundle;

import cloud.ptl.itemserver.persistence.dao.AbstractDAO;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

// TODO nullable = false set for all entities

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "bundle")
public class BundleDAO extends AbstractDAO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

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
