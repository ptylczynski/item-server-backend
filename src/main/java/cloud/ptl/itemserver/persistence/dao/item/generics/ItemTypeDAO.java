package cloud.ptl.itemserver.persistence.dao.item.generics;

import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity(name = "item_type")
@Inheritance(strategy = InheritanceType.JOINED)
public class ItemTypeDAO implements LongIndexed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty private String name;
    @NotEmpty  private String description;
}
