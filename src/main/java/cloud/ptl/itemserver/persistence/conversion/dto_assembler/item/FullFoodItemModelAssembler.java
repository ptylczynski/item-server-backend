package cloud.ptl.itemserver.persistence.conversion.dto_assembler.item;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.address.FullBundleModelAssembler;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.dto.item.FullFoodItemDTO;
import cloud.ptl.itemserver.persistence.helper.service.FoodItemService;
import cloud.ptl.itemserver.persistence.repositories.item.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FullFoodItemModelAssembler extends RepresentationModelAssemblerSupport<FoodItemDAO, FullFoodItemDTO> {

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodItemService foodItemService;

    public FullFoodItemModelAssembler() {
        super(FoodItemDAO.class, FullFoodItemDTO.class);
    }

    @Override
    public FullFoodItemDTO toModel(FoodItemDAO entity) {
        return FullFoodItemDTO.builder()
                .id(entity.getId())
                .fullBundleDTO(
                        this.getFullBundleDTO(entity.getBundleDAO())
                )
                .dateAdded(entity.getDateAdded())
                .dueDate(entity.getDueDate())
                .description(entity.getDescription())
                .name(entity.getName())
                .type(entity.getType())
                .build();
    }

    public FullFoodItemDTO toModel(Long id) throws ObjectNotFound {
        return this.toModel(
                this.foodItemService.findById(id)
        );
    }

    private FullBundleDTO getFullBundleDTO(BundleDAO bundleDAO){
        return this.fullBundleModelAssembler.toModel(bundleDAO);
    }
}
