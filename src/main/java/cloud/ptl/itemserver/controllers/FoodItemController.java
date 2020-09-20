package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.item.FullFoodItemModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dto.item.FullFoodItemDTO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodItemRepository;
import cloud.ptl.itemserver.persistence.validators.FoodItemValidator;
import cloud.ptl.itemserver.service.implementation.FoodItemService;
import cloud.ptl.itemserver.service.implementation.SecurityService;
import cloud.ptl.itemserver.service.implementation.UserService;
import cloud.ptl.itemserver.templates.ConfirmationTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/food")
public class FoodItemController {
    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodItemValidator foodItemValidator;

    @Autowired
    private FullFoodItemModelAssembler fullFoodItemModelAssembler;

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(foodItemValidator);
    }

    private final Logger logger = LoggerFactory.getLogger(FoodItemController.class);

//    @GetMapping("/number")
//    public EntityModel<Long> getNumber(){
//        this.logger.info("-----------");
//        this.logger.info("Checking number of all intems");
//        Long totalCount = this.foodItemRepository.count();
//        this.logger.debug("Total number of items is " + totalCount.toString() + '\n');
//        return EntityModel.of(
//                totalCount,
//                linkTo(methodOn(FoodItemController.class).getNumber()).withSelfRel()
//        );
//    }

//    @GetMapping("/ids")
//    public CollectionModel<Long> getIds(){
//        this.logger.info("-----------");
//        this.logger.info("Getting ids of all food items " + '\n');
//        List<FoodItemDAO> foods = this.foodItemService.findAll();
//        ArrayList<Long> ids = new ArrayList<>();
//        for(FoodItemDAO food : foods) ids.add(food.getId());
//        return CollectionModel.of(
//                ids,
//                linkTo(methodOn(FoodItemController.class).getIds()).withSelfRel()
//        );
//    }
 // TODO documentation
    @GetMapping("/{id}")
    public FullFoodItemDTO getOne(
            @PathVariable Long id) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Getting one item");
        this.logger.debug("Item id: " + id.toString());
        FoodItemDAO foodItemDAO = this.foodItemService.findById(id);
        this.foodItemService.hasAccess(foodItemDAO, AclPermission.VIEWER);
        this.logger.debug("Item found");
        this.logger.debug(foodItemDAO.toString());
        return this.fullFoodItemModelAssembler
                .toModel(foodItemDAO)
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
                size,
                page
        ));
        List<FoodItemDAO> foodItemDAOS =
                this.foodItemService.findAll(
                        PageRequest.of(page, size),
                        AclPermission.VIEWER
                );
        return this.fullFoodItemModelAssembler.toCollectionModel(foodItemDAOS)
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
        if(bindingResult.hasErrors()){
            logger.debug(
                    String.format("Food Item has errors: %s" + '\n', bindingResult.getAllErrors().toString())
            );
            throw new ObjectInvalid(
                    foodItemDAO,
                    bindingResult,
                    linkTo(FoodItemController.class).withSelfRel()
            );
        }
        this.logger.debug("Item is valid " + '\n');
        this.foodItemService.checkIfUserHasAccessToBundleAndType(foodItemDAO);
        foodItemDAO = foodItemRepository.save(foodItemDAO);
        this.securityService.grantPermission(foodItemDAO, AclPermission.EDITOR);
        this.logger.info("-----------");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.ADD,
                FoodItemDAO.class.getName(),
                linkTo(FoodItemController.class).withSelfRel()
        ).getEntityModel();
    }

    @DeleteMapping("/{id}")
    public EntityModel<String> delete(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.debug("Deleting item");
        this.logger.debug("Item id: " + id.toString());
        FoodItemDAO foodItemDAO = this.foodItemService.findById(id);
        this.foodItemService.hasAccess(foodItemDAO, AclPermission.EDITOR);
        this.logger.debug("Item deleted" + '\n');
        this.foodItemRepository.delete(foodItemDAO);
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
        FoodItemDAO oldFoodItem = this.foodItemService.findById(foodItemDAO.getId());
        this.foodItemService.hasAccess(oldFoodItem, AclPermission.EDITOR);
        this.logger.debug("Saving new food" + '\n');
        this.foodItemRepository.save(foodItemDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PUT,
                FoodItemController.class.getName(),
                linkTo(FoodItemController.class).withSelfRel()
        ).getEntityModel();
    }


    @PatchMapping("/add-editor/{id}")
    public EntityModel<String> addAsEditor(
            @PathVariable("id") Long foodItemId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Adding user as editor to food item");
        this.logger.debug("food item: " + foodItemId.toString());
        this.logger.debug("user: " + userId.toString());
        FoodItemDAO foodItemDAO = this.foodItemService.findById(foodItemId);
        this.foodItemService.hasAccess(foodItemDAO, AclPermission.EDITOR);
        this.securityService.grantPermission(
                foodItemDAO,
                AclPermission.EDITOR,
                this.userService.findById(userId)
        );
        this.logger.debug("User added");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserDAO.class.toString(),
                linkTo(
                        methodOn(FoodItemController.class).addAsEditor(foodItemId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/remove-editor/{id}")
    public EntityModel<String> removeAsEditor(
            @PathVariable("id") Long foodItemId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("removing user as editor of food item");
        this.logger.debug("user: " + userId.toString());
        this.logger.debug("food item: " + foodItemId.toString());
        FoodItemDAO foodItemDAO = this.foodItemService.findById(foodItemId);
        this.securityService.hasPermission(foodItemDAO, AclPermission.EDITOR);
        this.securityService.revokePermission(
                foodItemDAO,
                AclPermission.EDITOR,
                this.userService.findById(userId)
        );
        this.logger.debug("User removed");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserDAO.class.toString(),
                linkTo(
                        methodOn(FoodItemController.class).removeAsEditor(foodItemId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/add-viewer/{id}")
    public EntityModel<String> addAsViewer(
            @PathVariable("id") Long foodItemId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Adding user as viewer to food item");
        this.logger.debug("food item: " + foodItemId.toString());
        this.logger.debug("user: " + userId.toString());
        FoodItemDAO foodItemDAO = this.foodItemService.findById(foodItemId);
        this.foodItemService.hasAccess(foodItemDAO, AclPermission.EDITOR);
        this.securityService.grantPermission(
                foodItemDAO,
                AclPermission.VIEWER,
                this.userService.findById(userId)
        );
        this.logger.debug("User added");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserDAO.class.toString(),
                linkTo(
                        methodOn(FoodItemController.class).addAsEditor(foodItemId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/remove-viewer/{id}")
    public EntityModel<String> removeAsViewer(
            @PathVariable("id") Long foodItemId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("removing user as viewer of food item");
        this.logger.debug("user: " + userId.toString());
        this.logger.debug("food item: " + foodItemId.toString());
        FoodItemDAO foodItemDAO = this.foodItemService.findById(foodItemId);
        this.securityService.hasPermission(foodItemDAO, AclPermission.EDITOR);
        this.securityService.revokePermission(
                foodItemDAO,
                AclPermission.VIEWER,
                this.userService.findById(userId)
        );
        this.logger.debug("User removed");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserDAO.class.toString(),
                linkTo(
                        methodOn(FoodItemController.class).removeAsEditor(foodItemId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @GetMapping("/permissions/{id}")
    public CollectionModel<AclPermission> getPermissions(
            @PathVariable("id") Long id
    ) throws ObjectNotFound {
        FoodItemDAO foodItemDAO = this.foodItemService.findById(id);
        List<AclPermission> aclPermissions =
                this.securityService.acquiredPermissions(foodItemDAO);
        return CollectionModel.of(
                aclPermissions
        ).add(
                linkTo(FoodItemController.class).withSelfRel()
        );
    }
}
