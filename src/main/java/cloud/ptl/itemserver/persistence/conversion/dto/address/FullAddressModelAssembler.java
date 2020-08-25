package cloud.ptl.itemserver.persistence.conversion.dto.address;

import cloud.ptl.itemserver.persistence.conversion.dto.user.UserCensoredModelAssembler;
import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullAddressDTO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class FullAddressModelAssembler extends RepresentationModelAssemblerSupport<AddressDAO, FullAddressDTO> {

    @Autowired
    private UserCensoredModelAssembler userCensoredModelAssembler;

    public FullAddressModelAssembler() {
        super(AddressDAO.class, FullAddressDTO.class);
    }

    @Override
    public FullAddressDTO toModel(AddressDAO entity) {
        return FullAddressDTO.builder()
                .building(entity.getBuilding())
                .city(entity.getCity())
                .country(entity.getCountry())
                .home(entity.getHome())
                .street(entity.getStreet())
                .id(entity.getId())
                .zip(entity.getZip())
                .locators(
                        this.getUsers(entity.getLocators())
                )
                .build();
    }

    private Set<UserCensoredDTO> getUsers(Set<UserDAO> users){
        HashSet<UserCensoredDTO> result = new HashSet<>();
        for(UserDAO user : users){
            result.add(
                    this.userCensoredModelAssembler.toModel(user)
            );
        }
        return result;
    }
}
