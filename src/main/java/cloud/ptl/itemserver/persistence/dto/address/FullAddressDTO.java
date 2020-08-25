package cloud.ptl.itemserver.persistence.dto.address;

import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.projections.userDAO.UserCensored;
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
public class FullAddressDTO extends RepresentationModel<FullAddressDTO> {
    private Long id;
    private String city;
    private String country;
    private String street;
    private String building;
    private String home;
    private String zip;
    private Set<UserCensoredDTO> locators;
}
