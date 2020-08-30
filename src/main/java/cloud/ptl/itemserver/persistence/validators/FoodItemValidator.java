package cloud.ptl.itemserver.persistence.validators;

import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class FoodItemValidator implements Validator {

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(FoodItemDAO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if(!o.getClass().isAssignableFrom(FoodItemDAO.class)) return;
        else {
            FoodItemDAO foodItemDAO = (FoodItemDAO) o;

            LocalDate dateAdded = foodItemDAO.getDateAdded();
            LocalDate dueDate = foodItemDAO.getDueDate();

            // does due date exist in request
            if(dateAdded == null){
                errors.rejectValue("dueDate", "due.date.missing");
                return;
            }

            // does date added exist in request
            if(dueDate == null){
                errors.rejectValue("dateAdded", "date.added.missing");
                return;
            }

            // is due date after added date
            if(dueDate.isBefore(dateAdded)){
                errors.rejectValue("dueDate","due.date.before.date.added");
            }

            // does address exist
            BundleDAO bundleDAO = foodItemDAO.getBundleDAO();

            // does address exist in request
            if(bundleDAO == null){
                errors.rejectValue("addressDAO", "bundle.missing");
                return;
            }

            if(!bundleRepository.existsById(bundleDAO.getId())){
                errors.rejectValue("bundleDAO", "bundle.does.not.exist");
            }

            // does food type exist
            FoodTypeDAO foodTypeDAO = foodItemDAO.getType();

            // does food types exist in request
            if(foodTypeDAO == null){
                errors.rejectValue("type", "type.missing");
                return;
            }

            if(!foodTypeRepository.existsById(foodTypeDAO.getId())){
                errors.rejectValue("foodType", "food.type.does.not.exist");
            }
        }
    }
}
