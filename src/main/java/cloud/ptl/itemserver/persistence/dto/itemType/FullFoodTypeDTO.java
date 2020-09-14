package cloud.ptl.itemserver.persistence.dto.itemType;

import cloud.ptl.itemserver.persistence.dao.authorization.AclIdentityDAO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;
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
public class FullFoodTypeDTO extends RepresentationModel<FullFoodTypeDTO>
        implements LongIndexed {
    private Long id;
    private String name;
    private String description;
}
