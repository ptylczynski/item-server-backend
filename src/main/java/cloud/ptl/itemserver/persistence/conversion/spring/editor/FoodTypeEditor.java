package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Component
public class FoodTypeEditor extends PropertyEditorSupport {

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    private final Logger logger = LoggerFactory.getLogger(FoodTypeEditor.class);

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.logger.info("Transforming text into FoodTypeDAO");
        this.logger.debug("text=" + text);
        Optional<FoodTypeDAO> foodTypeDAO = foodTypeRepository.findById(
                Long.valueOf(text)
        );
        if(foodTypeDAO.isEmpty()) throw new IllegalArgumentException(
                String.format("Food type %s unformatable", text)
        );
        this.logger.debug("Transformed into: " + foodTypeDAO.get().toString());
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
