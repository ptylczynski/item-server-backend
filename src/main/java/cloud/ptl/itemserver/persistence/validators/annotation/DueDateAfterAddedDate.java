package cloud.ptl.itemserver.persistence.validators.annotation;

import cloud.ptl.itemserver.persistence.validators.implementations.DueDateAfterAddedDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = DueDateAfterAddedDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DueDateAfterAddedDate {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
