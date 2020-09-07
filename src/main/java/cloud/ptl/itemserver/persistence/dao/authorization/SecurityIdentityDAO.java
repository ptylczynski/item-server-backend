package cloud.ptl.itemserver.persistence.dao.authorization;

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
@Table(name = "security_identity")
@Builder
public class SecurityIdentityDAO implements LongIndexed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clazz;
    private Long objectId;
    private String securityHash;
}
