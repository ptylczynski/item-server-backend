package cloud.ptl.itemserver.persistence.dao.item.food;

import cloud.ptl.itemserver.controllers.BundleController;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.item.generics.ItemDAO;
import cloud.ptl.itemserver.persistence.validators.annotation.DueDateAfterAddedDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "food_item")
@PrimaryKeyJoinColumn
@DueDateAfterAddedDate
public class FoodItemDAO extends ItemDAO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "food_type_fk")
    @NotNull private FoodTypeDAO type;

    @ManyToOne
    @JoinColumn(name = "bundle_fk")
    private BundleDAO bundleDAO;
}
