package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.item.ObjectInvalid;
import cloud.ptl.itemserver.error.exception.item.ObjectNotFound;
import cloud.ptl.itemserver.error.resolver.manager.BasicErrorResolverManager;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodItemRepository;
import cloud.ptl.itemserver.persistence.validators.FoodItemValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/food")
public class FoodItemController {

    @Autowired
    BasicErrorResolverManager erm;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodItemValidator foodItemValidator;

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(foodItemValidator);
    }

    private Logger logger = LoggerFactory.getLogger(FoodItemController.class);

    @GetMapping("/{id}")
    public EntityModel<FoodItemDAO> getOne(
            @PathVariable Long id) throws ObjectNotFound {
        Optional<FoodItemDAO> foodItemDAO = foodItemRepository.findById(id);
        if(foodItemDAO.isEmpty())
            throw new ObjectNotFound(
                    id,
                    linkTo(
                            methodOn(FoodItemController.class).getOne(id)
                    ).withSelfRel()
            );
        return EntityModel.of(
                foodItemDAO.get(),
                linkTo(methodOn(FoodItemController.class).getOne(id)).withSelfRel());
    }

    @GetMapping("/all")
    public CollectionModel<FoodItemDAO> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Page<FoodItemDAO> foodItemDAOPage = foodItemRepository.findAll(
                PageRequest.of(page, size)
        );
        return CollectionModel.of(
                foodItemDAOPage.getContent(),
                linkTo(
                        methodOn(FoodItemController.class).getAll(page, size)
                ).withSelfRel()
        );
    }

    @PostMapping()
    public EntityModel<String> postOne(@Validated FoodItemDAO foodItemDAO,
                                       BindingResult bindingResult) throws ObjectInvalid {
        if(!bindingResult.hasErrors()){
            foodItemRepository.save(foodItemDAO);
            return EntityModel.of("Item saved",
                    linkTo(FoodItemController.class).withRel("get"));
        }
        else {
            logger.debug(
                    String.format("Food Item has errors: %s", bindingResult.getAllErrors().toString())
            );
            throw new ObjectInvalid(
                    foodItemDAO,
                    bindingResult,
                    linkTo(FoodItemController.class).withSelfRel()
            );
        }
    }
}
