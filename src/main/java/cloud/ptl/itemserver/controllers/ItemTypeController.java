package cloud.ptl.itemserver.controllers;


import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.resolver.manager.BasicErrorResolverManager;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import cloud.ptl.itemserver.templates.ConfirmationTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/type")
public class ItemTypeController {

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private BasicErrorResolverManager basicErrorResolverManager;

    private final Logger logger = LoggerFactory.getLogger(ItemTypeController.class);

    @GetMapping("/food/{id}")
    public EntityModel<FoodTypeDAO> foodTypeDAOEntityModel(
            @PathVariable Long id) throws ObjectNotFound {
        this.logger.info("Getting food item type id" + id.toString());
        Optional<FoodTypeDAO> foodTypeDAO = foodTypeRepository.findById(id);
        if(foodTypeDAO.isEmpty()) {
            this.logger.debug("Item does not exist");
            throw new ObjectNotFound(
                    id,
                    linkTo(
                            methodOn(ItemTypeController.class).foodTypeDAOEntityModel(id)
                    ).withSelfRel()
            );
        }
        return EntityModel.of(foodTypeDAO.get(),
                linkTo(methodOn(ItemTypeController.class).foodTypeDAOEntityModel(id)).withSelfRel());
    }

    @GetMapping("/food/all")
    public CollectionModel<FoodTypeDAO> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        this.logger.info("Getting all food types");
        Page<FoodTypeDAO> foodTypeDAOS = foodTypeRepository.findAll(
                PageRequest.of(page, size)
        );
        return CollectionModel.of(
                foodTypeDAOS.getContent(),
                linkTo(
                        methodOn(ItemTypeController.class).getAll(page, size)
                ).withSelfRel()
        );
    }

    @PostMapping("food")
    public EntityModel<String> stringEntityModel(
            FoodTypeDAO foodTypeDAO,
            BindingResult bindingResult) throws ObjectInvalid {
        this.logger.info("Saving new food item");
        this.logger.debug(foodTypeDAO.toString());
        if(bindingResult.hasErrors()) {
            this.logger.debug("New food type is invalid");
            this.logger.debug(bindingResult.getAllErrors().toString());
            throw new ObjectInvalid(
                    foodTypeDAO,
                    bindingResult,
                    linkTo(ItemTypeController.class).withSelfRel()
            );
        }
        this.foodTypeRepository.save(foodTypeDAO);

        return EntityModel.of("Food type added",
                linkTo(ItemTypeController.class).withRel("controller"));
    }

    @DeleteMapping("/food/{id}")
    public EntityModel<String> delete(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("Deleting food item type id " + id.toString());
        Optional<FoodTypeDAO> foodTypeDAO = this.foodTypeRepository.findById(id);
        if(foodTypeDAO.isEmpty()) {
            this.logger.debug("Food type does not exist");
            throw new ObjectNotFound(
                    id,
                    linkTo(
                            methodOn(ItemTypeController.class).delete(id)
                    ).withSelfRel()
            );
        }
        else{
            this.foodTypeRepository.delete(foodTypeDAO.get());
            return new ConfirmationTemplate(
                    ConfirmationTemplate.Token.DELETE,
                    FoodTypeDAO.class.getName(),
                    linkTo(
                            methodOn(ItemTypeController.class).delete(id)
                    ).withSelfRel()
            ).getEntityModel();
        }
    }

    @PutMapping("/food/{id}")
    public EntityModel<String> put(
            @ModelAttribute @Validated FoodTypeDAO foodTypeDAO,
            BindingResult bindingResult
    ) throws ObjectInvalid, ObjectNotFound {
        this.logger.info("Updating food item type");
        this.logger.debug(foodTypeDAO.toString());
        if(bindingResult.hasErrors()) {
            this.logger.debug("Food item type is invalid");
            this.logger.debug(bindingResult.getAllErrors().toString());
            throw new ObjectInvalid(
                    foodTypeDAO,
                    bindingResult,
                    linkTo(
                            methodOn(ItemTypeController.class).put(foodTypeDAO, bindingResult)
                    ).withSelfRel()
            );
        }
        Optional<FoodTypeDAO> oldFoodTypeDAO = this.foodTypeRepository.findById(foodTypeDAO.getId());
        if(oldFoodTypeDAO.isEmpty()) {
            this.logger.debug("Updating type does not exist");
            throw new ObjectNotFound(
                    foodTypeDAO.getId(),
                    linkTo(
                            methodOn(ItemTypeController.class).put(foodTypeDAO, bindingResult)
                    ).withSelfRel()
            );
        }
        this.foodTypeRepository.save(foodTypeDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PUT,
                ItemTypeController.class.getName(),
                linkTo(
                        methodOn(ItemTypeController.class).put(foodTypeDAO, bindingResult)
                ).withSelfRel()
        ).getEntityModel();
    }
}
