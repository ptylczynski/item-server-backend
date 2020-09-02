package cloud.ptl.itemserver.persistence.dao.item.generics;

import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "item_type")
@Inheritance(strategy = InheritanceType.JOINED)
public class ItemTypeDAO implements LongIndexed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_fk")
    private UserDAO owner;
}
