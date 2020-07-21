package cloud.ptl.itemserver.persistence.dao.item.food;

import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dao.item.generics.ItemDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "food_item")
@PrimaryKeyJoinColumn
public class FoodItemDAO extends ItemDAO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "food_type_fk")
    private FoodTypeDAO type;
}
