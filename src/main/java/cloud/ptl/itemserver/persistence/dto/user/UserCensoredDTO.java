package cloud.ptl.itemserver.persistence.dto.user;

import cloud.ptl.itemserver.persistence.dao.authorization.AclIdentityDAO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.helper.WithSecurityIdentity;
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
public class UserCensoredDTO extends RepresentationModel<UserCensoredDTO>
        implements LongIndexed {
    private Long id;
    private String username;
    private String displayName;
    private String mail;
}
