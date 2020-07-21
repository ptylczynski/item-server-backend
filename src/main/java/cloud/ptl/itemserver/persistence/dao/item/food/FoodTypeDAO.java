package cloud.ptl.itemserver.persistence.dao.item.food;

import cloud.ptl.itemserver.persistence.dao.item.generics.ItemTypeDAO;

import javax.persistence.*;
import java.util.List;

@Entity(name = "food_type")
@PrimaryKeyJoinColumn
public class FoodTypeDAO extends ItemTypeDAO {
}
