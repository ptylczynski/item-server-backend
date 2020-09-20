package cloud.ptl.itemserver.controllers;


import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.error.resolver.manager.BasicErrorResolverManager;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.itemType.FullFoodTypeModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.dto.itemType.FullFoodTypeDTO;
import cloud.ptl.itemserver.persistence.repositories.item.FoodTypeRepository;
import cloud.ptl.itemserver.service.implementation.FoodTypeService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/type")
public class ItemTypeController {

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private BasicErrorResolverManager basicErrorResolverManager;

    @Autowired
    private FullFoodTypeModelAssembler fullItemTypeModelAssembler;

    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(ItemTypeController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/food/{id}")
    public FullFoodTypeDTO foodTypeDAOEntityModel(
            @PathVariable Long id) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Getting food item type id" + id.toString());
        FoodTypeDAO foodTypeDAO = this.foodTypeService.findById(id);
        this.foodTypeService.hasAccess(foodTypeDAO, AclPermission.VIEWER);
        return fullItemTypeModelAssembler.toModel(foodTypeDAO)
                .add(
                        linkTo(
                                methodOn(ItemTypeController.class).foodTypeDAOEntityModel(id)
                        ).withSelfRel()
                );
    }

    @GetMapping("/food/all")
    public CollectionModel<FullFoodTypeDTO> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        this.logger.info("-----------");
        this.logger.info("Getting all food types");
        List<FoodTypeDAO> foodTypeDAOS =
                this.foodTypeService.findAll(
                        PageRequest.of(page, size),
                        AclPermission.VIEWER
                );
        return this.fullItemTypeModelAssembler.toCollectionModel(
                    foodTypeDAOS
                )
                .add(
                    linkTo(
                            methodOn(ItemTypeController.class).getAll(page, size)
                    ).withSelfRel()
                );
    }

    @PostMapping("/food")
    public EntityModel<String> stringEntityModel(
            FoodTypeDAO foodTypeDAO,
            BindingResult bindingResult) throws ObjectInvalid {
        this.logger.info("-----------");
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
        foodTypeDAO = this.foodTypeRepository.save(foodTypeDAO);
        this.securityService.grantPermission(foodTypeDAO, AclPermission.EDITOR);
        return EntityModel.of("Food type added",
                linkTo(ItemTypeController.class).withRel("controller"));
    }

    @DeleteMapping("/food/{id}")
    public EntityModel<String> delete(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Deleting food item type id " + id.toString());
        FoodTypeDAO foodTypeDAO = this.foodTypeService.findById(id);
        this.foodTypeService.hasAccess(foodTypeDAO, AclPermission.EDITOR);
        this.foodTypeRepository.delete(foodTypeDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.DELETE,
                FoodTypeDAO.class.getName(),
                linkTo(
                        methodOn(ItemTypeController.class).delete(id)
                ).withSelfRel()
        ).getEntityModel();

    }

    @PutMapping("/food/{id}")
    public EntityModel<String> put(
            @ModelAttribute @Validated FoodTypeDAO foodTypeDAO,
            BindingResult bindingResult
    ) throws ObjectInvalid, ObjectNotFound {
        this.logger.info("-----------");
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
        FoodTypeDAO oldFoodTypeDAO = this.foodTypeService.findById(foodTypeDAO.getId());
        this.foodTypeService.hasAccess(oldFoodTypeDAO, AclPermission.EDITOR);
        this.foodTypeRepository.save(foodTypeDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PUT,
                ItemTypeController.class.getName(),
                linkTo(
                        methodOn(ItemTypeController.class).put(foodTypeDAO, bindingResult)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/food/add-editor/{id}")
    public EntityModel<String> addAsEditor(
            @PathVariable("id") Long foodTypeId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Adding user as editor to food type");
        this.logger.debug("food type: " + foodTypeId.toString());
        this.logger.debug("user: " + userId.toString());
        FoodTypeDAO foodTypeDAO = this.foodTypeService.findById(foodTypeId);
        this.foodTypeService.hasAccess(foodTypeDAO, AclPermission.EDITOR);
        this.securityService.grantPermission(
                foodTypeDAO,
                AclPermission.EDITOR,
                this.userService.findById(userId)
        );
        this.logger.debug("User added");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserDAO.class.toString(),
                linkTo(
                        methodOn(ItemTypeController.class).addAsEditor(foodTypeId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/food/remove-editor/{id}")
    public EntityModel<String> removeAsEditor(
            @PathVariable("id") Long foodTypeId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("removing user as editor of food type");
        this.logger.debug("user: " + userId.toString());
        this.logger.debug("food type: " + foodTypeId.toString());
        FoodTypeDAO foodTypeDAO = this.foodTypeService.findById(foodTypeId);
        this.securityService.hasPermission(foodTypeDAO, AclPermission.EDITOR);
        this.securityService.revokePermission(
                foodTypeDAO,
                AclPermission.EDITOR,
                this.userService.findById(userId)
        );
        this.logger.debug("User removed");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserDAO.class.toString(),
                linkTo(
                        methodOn(ItemTypeController.class).removeAsEditor(foodTypeId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }


    @PatchMapping("/food/add-viewer/{id}")
    public EntityModel<String> addAsViewer(
            @PathVariable("id") Long foodTypeId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Adding user as viewer to food type");
        this.logger.debug("food type: " + foodTypeId.toString());
        this.logger.debug("user: " + userId.toString());
        FoodTypeDAO foodTypeDAO = this.foodTypeService.findById(foodTypeId);
        this.foodTypeService.hasAccess(foodTypeDAO, AclPermission.EDITOR);
        this.securityService.grantPermission(
                foodTypeDAO,
                AclPermission.VIEWER,
                this.userService.findById(userId)
        );
        this.logger.debug("User added");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserDAO.class.toString(),
                linkTo(
                        methodOn(ItemTypeController.class).addAsViewer(foodTypeId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/food/remove-viewer/{id}")
    public EntityModel<String> removeAsViewer(
            @PathVariable("id") Long foodTypeId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("removing user as viewer of food type");
        this.logger.debug("user: " + userId.toString());
        this.logger.debug("bundle: " + foodTypeId.toString());
        FoodTypeDAO foodTypeDAO = this.foodTypeService.findById(foodTypeId);
        this.securityService.hasPermission(foodTypeDAO, AclPermission.EDITOR);
        this.securityService.revokePermission(
                foodTypeDAO,
                AclPermission.EDITOR,
                this.userService.findById(userId)
        );
        this.logger.debug("User removed");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                UserDAO.class.toString(),
                linkTo(
                        methodOn(ItemTypeController.class).removeAsViewer(foodTypeId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @GetMapping("/food/permissions/{id}")
    public CollectionModel<AclPermission> aclPermissions(
            @PathVariable("id") Long id
    ) throws ObjectNotFound {
        FoodTypeDAO foodTypeDAO = this.foodTypeService.findById(id);
        List<AclPermission> permissions =
                this.securityService.acquiredPermissions(foodTypeDAO);
        return CollectionModel.of(
                permissions
        ).add(
                linkTo(ItemTypeController.class).withSelfRel()
        );
    }
}
