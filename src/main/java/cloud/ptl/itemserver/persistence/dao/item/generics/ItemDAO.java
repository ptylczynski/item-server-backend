package cloud.ptl.itemserver.persistence.dao.item.generics;

import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity(name = "item")
@Inheritance(strategy = InheritanceType.JOINED)
public class ItemDAO implements LongIndexed, WithSecurityIdentity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String securityHash;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAdded;

    @ManyToOne
    @JoinColumn(name = "bundle_fk")
    private BundleDAO bundleDAO;
}
