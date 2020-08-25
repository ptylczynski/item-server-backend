package cloud.ptl.itemserver.persistence.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@Relation(itemRelation = "user", collectionRelation = "users")
public class UserCensoredDTO extends RepresentationModel<UserCensoredDTO> {
    private Long id;
    private String username;
    private String displayName;
    private String mail;
}
