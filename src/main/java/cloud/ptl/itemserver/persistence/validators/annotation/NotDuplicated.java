package cloud.ptl.itemserver.persistence.validators.annotation;


import cloud.ptl.itemserver.persistence.validators.implementations.NotDuplicatedValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotDuplicatedValidator.class)
@Documented
public @interface NotDuplicated {
    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    NotDuplicatedValidator.Entity value();

    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        NotDuplicated[] value();
    }
}
