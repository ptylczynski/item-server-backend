package cloud.ptl.itemserver.persistence.conversion.dto.itemType;

import cloud.ptl.itemserver.persistence.dao.item.generics.ItemTypeDAO;
import cloud.ptl.itemserver.persistence.dto.item.FullFoodItemDTO;
import cloud.ptl.itemserver.persistence.dto.itemType.FullItemTypeDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class FullItemTypeModelAssembler extends RepresentationModelAssemblerSupport<ItemTypeDAO, FullItemTypeDTO> {
    public FullItemTypeModelAssembler() {
        super(ItemTypeDAO.class, FullItemTypeDTO.class);
    }

    @Override
    public FullItemTypeDTO toModel(ItemTypeDAO entity) {
        return FullItemTypeDTO.builder()
                .description(entity.getDescription())
                .name(entity.getName())
                .id(entity.getId())
                .build();
    }
}
