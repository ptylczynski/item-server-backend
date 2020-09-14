package cloud.ptl.itemserver.persistence.dao.authorization;

import cloud.ptl.itemserver.persistence.dao.authentication.AuthorityDAO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "acl_identity")
@Builder
public class AclIdentityDAO implements LongIndexed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class")
    private String clazz;

    @Column(name = "object_id")
    private Long objectId;
}
