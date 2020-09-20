package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.controllers.BundleController;
import cloud.ptl.itemserver.controllers.ItemTypeController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.permission.InsufficientPermission;
import cloud.ptl.itemserver.persistence.dao.authorization.AclEntryDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
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
public class FoodTypeService {
    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(FoodTypeService.class);

    public Boolean checkIfFoodTypeExists(Long id) throws ObjectNotFound {
        this.logger.info("Checking if food type exists");
        this.logger.debug("id=" + id);
        if(!this.foodTypeRepository.existsById(id)){
            this.logger.debug("Food type does not exists");
            throw new ObjectNotFound(
                    FoodTypeDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(ItemTypeController.class).withSelfRel()
            );
        }
        this.logger.debug("food type exists");
        return true;
    }

    public FoodTypeDAO findById(Long id) throws ObjectNotFound {
        this.logger.info("Searching food type");
        this.logger.debug("id=" + id);
        this.checkIfFoodTypeExists(id);
        return this.foodTypeRepository.findById(id).get();
    }

    public List<FoodTypeDAO> findAll(Pageable pageable, AclPermission permission){
        this.logger.info("Fetching all accessiable food type from db");
        List<AclEntryDAO> aclEntryDAOS =
                this.securityService.getAllAccesiableAclEntries(
                        FoodTypeDAO.class,
                        pageable
                );
        List<FoodTypeDAO> foodTypeDAOS =
                this.foodTypeRepository.findAllById(
                    aclEntryDAOS.stream()
                            .peek(a -> this.logger.debug(a.toString()))
                        // filter for only acl entries with given or higher permission
                        .filter(a -> a.getAclPermissions().stream()
                                .anyMatch(permission::sameOrLower))
                        // extract ids
                        .map(a -> a.getAclIdentityDAO().getObjectId())
                        .collect(Collectors.toSet())
        );
        this.logger.debug("acl entry: " + aclEntryDAOS.toString());
        this.logger.debug("food types: " + foodTypeDAOS.toString());
        return foodTypeDAOS;
    }

    public Boolean hasAccess(FoodTypeDAO foodTypeDAO, AclPermission permission){
        if(this.securityService.hasPermission(foodTypeDAO, permission)) return true;
        else throw new InsufficientPermission(
                FoodTypeDAO.class.getCanonicalName(),
                permission,
                WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
        );
    }
}
