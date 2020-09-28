package cloud.ptl.itemserver.persistence.validators.implementations;

import cloud.ptl.itemserver.BeanInjector;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import cloud.ptl.itemserver.persistence.validators.annotation.HasAccess;
import cloud.ptl.itemserver.service.implementation.BundleService;
import cloud.ptl.itemserver.service.implementation.FoodItemService;
import cloud.ptl.itemserver.service.implementation.FoodTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasAccessValidator implements ConstraintValidator<HasAccess, LongIndexed> {

    private final BundleService bundleService = (BundleService) BeanInjector.getBean(BundleService.class);
    private final FoodTypeService foodTypeService = (FoodTypeService) BeanInjector.getBean(FoodTypeService.class);
    private final FoodItemService foodItemService = (FoodItemService) BeanInjector.getBean(FoodItemService.class);

    private AclPermission aclPermission;
    private final Logger logger = LoggerFactory.getLogger(HasAccessValidator.class);

    @Override
    public void initialize(HasAccess constraintAnnotation) {
        this.aclPermission = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LongIndexed longIndexed, ConstraintValidatorContext constraintValidatorContext) {
        this.logger.info("Validating " + longIndexed.getClass().toString() + " with id " + longIndexed.getId());
        switch(longIndexed.getClass().toString()){
            case "BundleDAO" :{
                return bundleService.hasAccess(
                        (BundleDAO) longIndexed,
                        this.aclPermission
                );
            }
            case "FoodTypeDAO": {
                return this.foodTypeService.hasAccess(
                        (FoodTypeDAO) longIndexed,
                        this.aclPermission
                );
            }
            case "FoodItemDAO": {
                return this.foodItemService.hasAccess(
                        (FoodItemDAO) longIndexed,
                        this.aclPermission
                );
            }
            default:
                throw new IllegalStateException("Unexpected value: " + longIndexed.getClass().toString());
        }
    }
}
