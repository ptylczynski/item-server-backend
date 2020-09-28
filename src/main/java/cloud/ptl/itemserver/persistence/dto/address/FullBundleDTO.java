package cloud.ptl.itemserver.persistence.dto.address;

import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(itemRelation = "address", collectionRelation = "addresses")
public class FullBundleDTO extends RepresentationModel<FullBundleDTO>
    implements LongIndexed {
    private Long id;
    private String description;
    private String name;
}



