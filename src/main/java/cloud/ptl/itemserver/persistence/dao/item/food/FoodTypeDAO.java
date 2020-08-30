package cloud.ptl.itemserver.persistence.dao.item.food;

import cloud.ptl.itemserver.persistence.dao.item.generics.ItemTypeDAO;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

@Entity(name = "food_type")
@PrimaryKeyJoinColumn
public class FoodTypeDAO extends ItemTypeDAO {
    @OneToMany
    @JoinColumn(name = "type_fk")
    private List<FoodItemDAO> itemsOfType;
}
