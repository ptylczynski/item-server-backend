package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.controllers.ItemTypeController;
import cloud.ptl.itemserver.error.exception.parsing.ObjectUnformatable;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.itemType.FullFoodTypeModelAssembler;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Component
public class FoodTypeEditor extends PropertyEditorSupport {

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private FullFoodTypeModelAssembler fullFoodTypeModelAssembler;

    private final Logger logger = LoggerFactory.getLogger(FoodTypeEditor.class);

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.logger.info("Transforming text into FoodTypeDTO");
        this.logger.debug("text=" + text);
        Optional<FoodTypeDAO> foodTypeDAO = foodTypeRepository.findById(
                Long.valueOf(text)
        );
        if(foodTypeDAO.isEmpty()){
            this.logger.debug("Object unformatable");
            this.logger.debug("object: " + text);
            throw new ObjectUnformatable(
                    WebMvcLinkBuilder.linkTo(ItemTypeController.class).withSelfRel()
            );
        }
        this.setValue(foodTypeDAO.get());
    }

    @Override
    public String getAsText() {
        this.logger.debug("Transforming FoodTypeDAO into text");
        FoodTypeDAO foodTypeDAO = (FoodTypeDAO) this.getValue();
        Long id = foodTypeDAO.getId();
        return String.valueOf(id);
    }
}
