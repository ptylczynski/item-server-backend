package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.controllers.FoodItemController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.authorization.AclEntryDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(FoodItemService.class);

    public Boolean checkIfFoodItemExists(Long id) throws ObjectNotFound {
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
        this.checkIfFoodItemExists(id);
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
        throw new AccessDeniedException("Access Denied");
    }
}
