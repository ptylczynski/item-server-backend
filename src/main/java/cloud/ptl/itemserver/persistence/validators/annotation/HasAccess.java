package cloud.ptl.itemserver.persistence.validators.annotation;

import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.validators.implementations.HasAccessValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Constraint(validatedBy = HasAccessValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasAccess {
    String message() default "";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    AclPermission value() default AclPermission.VIEWER;
}
