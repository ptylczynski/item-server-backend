package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.controllers.BundleController;
import cloud.ptl.itemserver.controllers.FoodItemController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.permission.InsufficientPermission;
import cloud.ptl.itemserver.persistence.dao.authorization.AclEntryDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodItemRepository;
import cloud.ptl.itemserver.service.abstract2.AbstractDAOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodItemService extends AbstractDAOService<FoodItemDAO> {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(FoodItemService.class);

    @Autowired
    private BundleService bundleService;

    @Autowired
    private FoodTypeService foodTypeService;

    public Boolean checkIfExists(Long id) throws ObjectNotFound {
        this.logger.info("Checking if item exists");
        this.logger.debug("Id: " + id);
        if(!this.foodItemRepository.existsById(id)){
            this.logger.debug("Item does not exist");
            throw new ObjectNotFound(
                    FoodItemDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(FoodItemController.class).withSelfRel()
            );
        }
        this.logger.debug("Item exists");
        return true;
    }

    public FoodItemDAO findById(Long id) throws ObjectNotFound {
        this.logger.info("Searching food item");
        this.logger.debug("id=" + id);
        this.checkIfExists(id);
        return this.foodItemRepository.findById(id).get();
    }

    public List<FoodItemDAO> findAll(Pageable pageable, AclPermission permission){
        this.logger.info("Fetching all accesiable food items from db");
        List<AclEntryDAO> aclEntryDAOS =
                this.securityService.getAllAccesiableAclEntries(
                        FoodItemDAO.class,
                        pageable
                );
        this.logger.debug("acl entries: " + aclEntryDAOS.toString());
        List<FoodItemDAO> items =
                this.foodItemRepository.findAllById(
                aclEntryDAOS.stream()
                        // filter for only acl entries with given or higher permission
                        .filter(a ->  a.getAclPermissions().stream()
                                        .anyMatch(permission::sameOrLower))
                        // extract ids
                        .map(a -> a.getAclIdentityDAO().getObjectId())
                        .collect(Collectors.toSet())
        );
        this.logger.debug("food items: " + items);
        return items;
    }

    public Boolean hasAccess(FoodItemDAO foodItemDAO, AclPermission permission){
        if(this.securityService.hasPermission(foodItemDAO, permission)) return true;
        else throw new InsufficientPermission(
                FoodItemDAO.class.getCanonicalName(),
                permission,
                WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
        );
    }


    public Boolean checkIfUserHasAccessToBundleAndType(FoodItemDAO foodItem){
        this.logger.info("Checking if user has access to food type and bundle");
        FoodTypeDAO foodTypeDAO = foodItem.getType();
        BundleDAO bundleDAO = foodItem.getBundleDAO();
        this.logger.debug("food type: " + foodTypeDAO.toString());
        this.logger.debug("bundle: " + bundleDAO.toString());
        this.foodTypeService.hasAccess(foodTypeDAO, AclPermission.EDITOR);
        this.bundleService.hasAccess(bundleDAO, AclPermission.EDITOR);
        return true;
    }
}
