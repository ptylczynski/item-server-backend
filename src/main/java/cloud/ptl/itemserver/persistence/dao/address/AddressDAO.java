package cloud.ptl.itemserver.persistence.dao.address;

import cloud.ptl.itemserver.persistence.dao.AbstractDAO;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.item.generics.ItemDAO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "locatorOf", fetch = FetchType.LAZY)
    private Set<UserDAO> locators;

}
