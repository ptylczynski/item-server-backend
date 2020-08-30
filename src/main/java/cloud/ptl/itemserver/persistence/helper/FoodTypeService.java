package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.controllers.ItemTypeController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

@Service
public class FoodTypeService {
    @Autowired
    private FoodTypeRepository foodTypeRepository;

    private final Logger logger = LoggerFactory.getLogger(FoodTypeService.class);

    public Boolean checkIfFoodTypeExists(Long id) throws ObjectNotFound {
        this.logger.info("Checking if food type exists");
        this.logger.debug("id=" + id);
        if(!this.foodTypeRepository.existsById(id)){
            this.logger.debug("Food type does not exists");
            throw new ObjectNotFound(
                    id,
                    WebMvcLinkBuilder.linkTo(ItemTypeController.class).withSelfRel()
            );
        }
        this.logger.debug("food type exists");
        return true;
    }

    public FoodTypeDAO getById(Long id) throws ObjectNotFound {
        this.logger.info("Searching food type");
        this.logger.debug("id=" + id);
        this.checkIfFoodTypeExists(id);
        return this.foodTypeRepository.findById(id).get();
    }
}
