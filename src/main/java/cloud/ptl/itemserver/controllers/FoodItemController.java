package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.error.resolver.manager.BasicErrorResolverManager;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.item.FullFoodItemModelAssembler;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dto.item.FullFoodItemDTO;
import cloud.ptl.itemserver.persistence.helper.FoodItemService;
import cloud.ptl.itemserver.persistence.repositories.item.FoodItemRepository;
import cloud.ptl.itemserver.persistence.validators.FoodItemValidator;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private FullFoodItemModelAssembler fullFoodItemModelAssembler;

    @Autowired
    private FoodItemService foodItemService;

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(foodItemValidator);
    }

    private final Logger logger = LoggerFactory.getLogger(FoodItemController.class);

    @GetMapping("/number")
    public EntityModel<Long> getNumber(){
        this.logger.info("-----------");
        this.logger.info("Checking number of all intems");
        Long totalCount = this.foodItemRepository.count();
        this.logger.debug("Total number of items is " + totalCount.toString() + '\n');
        return EntityModel.of(
                totalCount,
                linkTo(methodOn(FoodItemController.class).getNumber()).withSelfRel()
        );
    }

    @GetMapping("/ids")
    public CollectionModel<Long> getIds(){
        this.logger.info("-----------");
        this.logger.info("Getting ids of all food items " + '\n');
        List<FoodItemDAO> foods = this.foodItemRepository.findAll();
        ArrayList<Long> ids = new ArrayList<>();
        for(FoodItemDAO food : foods) ids.add(food.getId());
        return CollectionModel.of(
                ids,
                linkTo(methodOn(FoodItemController.class).getIds()).withSelfRel()
        );
    }
 // TODO documentation
    @GetMapping("/{id}")
    public FullFoodItemDTO getOne(
            @PathVariable Long id) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Getting one item");
        this.logger.debug("Item id: " + id.toString());
        Optional<FoodItemDAO> foodItemDAO = foodItemRepository.findById(id);
        this.foodItemService.checkIfFoodItemExists(id);
        this.logger.debug("Item found");
        this.logger.debug(foodItemDAO.get().toString());
        return this.fullFoodItemModelAssembler
                .toModel(foodItemDAO.get())
                .add(
                        linkTo(
                                methodOn(FoodItemController.class).getOne(id)
                        ).withSelfRel()
                );
    }

    @GetMapping("/all")
    public CollectionModel<FullFoodItemDTO> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        this.logger.info("-----------");
        this.logger.info(String.format(
                "Getting info of %d items on page %d" + '\n',
                page,
                size
        ));
        Page<FoodItemDAO> foodItemDAOPage = foodItemRepository.findAll(
                PageRequest.of(page, size)
        );
        return this.fullFoodItemModelAssembler.toCollectionModel(foodItemDAOPage.getContent())
                .add(
                    linkTo(
                            methodOn(FoodItemController.class).getAll(page, size)
                    ).withSelfRel()
                );
    }

    @PostMapping()
    public EntityModel<String> postOne(@Validated FoodItemDAO foodItemDAO,
                                       BindingResult bindingResult) throws ObjectInvalid {
        this.logger.info("-----------");
        this.logger.info("Saving food");
        this.logger.debug(foodItemDAO.toString());
        if(!bindingResult.hasErrors()){
            this.logger.debug("Item is valid " + '\n');
            foodItemRepository.save(foodItemDAO);
            this.logger.info("-----------");
            return new ConfirmationTemplate(
                    ConfirmationTemplate.Token.ADD,
                    FoodItemDAO.class.getName(),
                    linkTo(FoodItemController.class).withSelfRel()
            ).getEntityModel();
        }
        else {
            logger.debug(
                    String.format("Food Item has errors: %s" + '\n', bindingResult.getAllErrors().toString())
            );
            throw new ObjectInvalid(
                    foodItemDAO,
                    bindingResult,
                    linkTo(FoodItemController.class).withSelfRel()
            );
        }
    }

    @DeleteMapping("/{id}")
    public EntityModel<String> delete(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.debug("Deleting item");
        this.logger.debug("Item id: " + id.toString());
        Optional<FoodItemDAO> foodItemDAO = this.foodItemRepository.findById(id);
        this.foodItemService.checkIfFoodItemExists(id);
        this.logger.debug("Item deleted" + '\n');
        this.foodItemRepository.delete(foodItemDAO.get());
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.DELETE,
                FoodItemDAO.class.getName(),
                linkTo(
                        methodOn(FoodItemController.class).delete(id)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PutMapping("/{id}")
    public EntityModel<String> put(
            @ModelAttribute @Validated FoodItemDAO foodItemDAO,
            BindingResult bindingResult
    ) throws ObjectInvalid, ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Updating food");
        this.logger.debug(foodItemDAO.toString());
        if(bindingResult.hasErrors()) {
            this.logger.debug("Food has errors" + '\n');
            this.logger.debug(bindingResult.getAllErrors().toString());
            throw new ObjectInvalid(
                    foodItemDAO,
                    bindingResult,
                    linkTo(FoodItemController.class).withSelfRel()
            );
        }
        Optional<FoodItemDAO> oldFoodItem = this.foodItemRepository.findById(foodItemDAO.getId());
        this.foodItemService.checkIfFoodItemExists(foodItemDAO.getId());
        this.logger.debug("Saving new food" + '\n');
        this.foodItemRepository.save(foodItemDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PUT,
                FoodItemController.class.getName(),
                linkTo(FoodItemController.class).withSelfRel()
        ).getEntityModel();
    }
}
