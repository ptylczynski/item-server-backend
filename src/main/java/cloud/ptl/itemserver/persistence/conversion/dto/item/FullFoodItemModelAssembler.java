package cloud.ptl.itemserver.persistence.conversion.dto.item;

import cloud.ptl.itemserver.persistence.conversion.dto.address.FullAddressModelAssembler;
import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullAddressDTO;
import cloud.ptl.itemserver.persistence.dto.item.FullFoodItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FullFoodItemModelAssembler extends RepresentationModelAssemblerSupport<FoodItemDAO, FullFoodItemDTO> {

    @Autowired
    private FullAddressModelAssembler fullAddressModelAssembler;

    public FullFoodItemModelAssembler() {
        super(FoodItemDAO.class, FullFoodItemDTO.class);
    }

    @Override
    public FullFoodItemDTO toModel(FoodItemDAO entity) {
        return FullFoodItemDTO.builder()
                .id(entity.getId())
                .addressDTO(
                        this.getAddressDTO(entity.getAddressDAO())
                )
                .dateAdded(entity.getDateAdded())
                .dueDate(entity.getDueDate())
                .description(entity.getDescription())
                .name(entity.getName())
                .type(entity.getType())
                .build();
    }

    private FullAddressDTO getAddressDTO(AddressDAO addressDAO){
        return this.fullAddressModelAssembler.toModel(addressDAO);
    }
}
