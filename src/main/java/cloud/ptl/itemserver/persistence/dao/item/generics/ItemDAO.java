package cloud.ptl.itemserver.persistence.dao.item.generics;

import cloud.ptl.itemserver.persistence.dao.AbstractDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "item")
@Inheritance(strategy = InheritanceType.JOINED)
public class ItemDAO extends AbstractDAO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAdded;

    @ManyToOne
    @JoinColumn(name = "bundle_fk")
    private BundleDAO bundleDAO;
}
