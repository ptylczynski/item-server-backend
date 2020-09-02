package cloud.ptl.itemserver.persistence.conversion.dto_assembler.itemType;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.item.generics.ItemTypeDAO;
import cloud.ptl.itemserver.persistence.dto.itemType.FullFoodTypeDTO;
import cloud.ptl.itemserver.persistence.helper.service.FoodTypeService;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class FullFoodTypeModelAssembler extends RepresentationModelAssemblerSupport<ItemTypeDAO, FullFoodTypeDTO> {

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private FoodTypeService foodTypeService;

    public FullFoodTypeModelAssembler() {
        super(ItemTypeDAO.class, FullFoodTypeDTO.class);
    }

    @Override
    public FullFoodTypeDTO toModel(ItemTypeDAO entity) {
        return FullFoodTypeDTO.builder()
                .description(entity.getDescription())
                .name(entity.getName())
                .id(entity.getId())
                .build();
    }

    public FullFoodTypeDTO toModel(Long id) throws ObjectNotFound {
        return this.toModel(
                this.foodTypeService.getById(id)
        );
    }
}
