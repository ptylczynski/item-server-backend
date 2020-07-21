package cloud.ptl.itemserver.persistence.dao.address;

import cloud.ptl.itemserver.persistence.dao.AbstractDAO;
import cloud.ptl.itemserver.persistence.dao.item.generics.ItemDAO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "address")
public class AddressDAO extends AbstractDAO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String country;
    private String street;
    private String building;
    private String home;
    private String zip;

}
