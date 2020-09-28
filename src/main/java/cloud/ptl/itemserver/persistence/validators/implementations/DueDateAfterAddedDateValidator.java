package cloud.ptl.itemserver.persistence.validators.implementations;

import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.validators.annotation.DueDateAfterAddedDate;
import org.springframework.cglib.core.Local;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Date;

public class DueDateAfterAddedDateValidator implements ConstraintValidator<DueDateAfterAddedDate, FoodItemDAO> {

    @Override
    public void initialize(DueDateAfterAddedDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(FoodItemDAO foodItemDAO, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate dueDate = foodItemDAO.getDueDate();
        LocalDate dateAdded = foodItemDAO.getDateAdded();
        return dateAdded.isBefore(dueDate);
    }
}
