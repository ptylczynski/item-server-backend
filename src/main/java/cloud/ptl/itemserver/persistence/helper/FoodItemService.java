package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.controllers.FoodItemController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    private final Logger logger = LoggerFactory.getLogger(FoodItemService.class);

    public Boolean checkIfFoodItemExists(Long id) throws ObjectNotFound {
        this.logger.info("Checking if item exists");
        this.logger.debug("Id: " + id);
        if(!this.foodItemRepository.existsById(id)){
            this.logger.debug("Item does not exist");
            throw new ObjectNotFound(
                    id,
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
}
