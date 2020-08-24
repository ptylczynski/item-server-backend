package cloud.ptl.itemserver.persistence.conversion.spring;

import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Optional;

@Component
public class FoodTypeEditor extends PropertyEditorSupport {

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Optional<FoodTypeDAO> foodTypeDAO = foodTypeRepository.findById(
                Long.valueOf(text)
        );
        if(foodTypeDAO.isEmpty()) throw new IllegalArgumentException(
                String.format("Food type %s unformatable", text)
        );
        this.setValue(foodTypeDAO.get());
    }

    @Override
    public String getAsText() {
        FoodTypeDAO foodTypeDAO = (FoodTypeDAO) this.getValue();
        Long id = foodTypeDAO.getId();
        return String.valueOf(id);
    }
}
