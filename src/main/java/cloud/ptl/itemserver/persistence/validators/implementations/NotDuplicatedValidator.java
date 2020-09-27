package cloud.ptl.itemserver.persistence.validators.implementations;

import cloud.ptl.itemserver.BeanInjector;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import cloud.ptl.itemserver.persistence.validators.annotation.NotDuplicated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotDuplicatedValidator implements ConstraintValidator<NotDuplicated, Object> {

    public enum Entity{
        EMAIL, USERNAME
    }

    private Entity entity;

    private final UserRepository userRepository =
            (UserRepository) BeanInjector.getBean(UserRepository.class);

    @Override
    public void initialize(NotDuplicated constraintAnnotation) {
        this.entity = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        switch (this.entity){
            case EMAIL:{
                return !this.userRepository.existsByMail(
                        (String) o
                );
            }
            case USERNAME:{
                return !this.userRepository.existsByUsername(
                        (String) o
                );
            }
        }
        return false;
    }
}
