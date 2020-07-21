package cloud.ptl.itemserver.persistence.validators;

import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.address.AddressRepository;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class FoodItemValidator implements Validator {

    @Autowired
    private AddressRepository addressRepository;

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

            // is due date after added date
            LocalDate dateAdded = foodItemDAO.getDateAdded();
            LocalDate dueDate = foodItemDAO.getDueDate();
            if(dueDate.isBefore(dateAdded)){
                errors.rejectValue("dueDate","due.date.before.date.added");
            }

            // does address exist
            AddressDAO addressDAO = foodItemDAO.getAddressDAO();
            if(!addressRepository.existsById(addressDAO.getId())){
                errors.rejectValue("addressDAO", "storage.address.does.not.exist");
            }

            // does food type exist
            FoodTypeDAO foodTypeDAO = foodItemDAO.getType();
            if(!foodTypeRepository.existsById(foodTypeDAO.getId())){
                errors.rejectValue("foodType", "food.type.does.not.exist");
            }
        }
    }
}
