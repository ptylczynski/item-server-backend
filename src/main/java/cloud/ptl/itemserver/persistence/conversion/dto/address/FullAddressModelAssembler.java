package cloud.ptl.itemserver.persistence.conversion.dto.address;

import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullAddressDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FullAddressModelAssembler extends RepresentationModelAssemblerSupport<AddressDAO, FullAddressDTO> {
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
                .build();
    }
}
