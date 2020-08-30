package cloud.ptl.itemserver.persistence.dto.address;

import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(itemRelation = "address", collectionRelation = "addresses")
public class FullBundleDTO extends RepresentationModel<FullBundleDTO> {
    private Long id;
    private String description;
    private UserCensoredDTO owner;
    private Set<UserCensoredDTO> editors;
    private Set<UserCensoredDTO> viewers;
}



