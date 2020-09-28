package cloud.ptl.itemserver.persistence.dto.item;

import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
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
public class FullFoodItemDTO extends RepresentationModel<FullFoodItemDTO>
        implements LongIndexed {
    private Long id;
    private String name;
    private String description;
    private LocalDate dateAdded;
    private FullBundleDTO fullBundleDTO;
    private FoodTypeDAO type;
    private LocalDate dueDate;
}
