package cloud.ptl.itemserver.persistence.dao.bundle;

import cloud.ptl.itemserver.persistence.dao.authorization.AclIdentityDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity(name = "bundle")
public class BundleDAO implements LongIndexed {
    public enum Type {
        FOOD;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty private String name;
    @NotEmpty private String description;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Type type;

    @OneToMany(mappedBy = "bundleDAO")
    private List<FoodItemDAO> foodItemDAOS;
}
