package cloud.ptl.itemserver.persistence.dto.itemType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(itemRelation = "type", collectionRelation = "types")
public class FullFoodTypeDTO extends RepresentationModel<FullFoodTypeDTO> {
    private long id;
    private String name;
    private String description;
}
