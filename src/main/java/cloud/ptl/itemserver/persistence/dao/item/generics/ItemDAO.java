package cloud.ptl.itemserver.persistence.dao.item.generics;

import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity(name = "item")
@Inheritance(strategy = InheritanceType.JOINED)
public class ItemDAO implements LongIndexed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty private String name;
    @NotEmpty private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dateAdded;

    @ManyToOne
    @JoinColumn(name = "bundle_fk")
    private BundleDAO bundleDAO;
}
