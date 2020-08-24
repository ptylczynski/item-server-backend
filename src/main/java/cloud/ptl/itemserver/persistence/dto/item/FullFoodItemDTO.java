package cloud.ptl.itemserver.persistence.dto.item;

import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(itemRelation = "food", collectionRelation = "foods")
public class FullFoodItemDTO extends RepresentationModel<FullFoodItemDTO> {
    private long id;

    private String name;
    private String description;
    private LocalDate dateAdded;
    private AddressDAO addressDAO;
    private FoodTypeDAO type;
    private LocalDate dueDate;
}
