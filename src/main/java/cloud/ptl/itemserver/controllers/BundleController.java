package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.address.FullBundleModelAssembler;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.item.FullFoodItemModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.dto.item.FullFoodItemDTO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import cloud.ptl.itemserver.service.implementation.BundleService;
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
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/bundle")
public class BundleController {
    @Autowired
    BundleRepository bundleRepository;

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(BundleController.class);

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

    @Autowired
    private FullFoodItemModelAssembler fullFoodItemModelAssembler;

    @Autowired
    private BundleService bundleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/{id}")
    FullBundleDTO getFull(@PathVariable("id") Long id) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Getting bundle with id: " + id.toString());
        BundleDAO bundleDAO = this.bundleService.findById(id);
        this.bundleService.hasAccess(bundleDAO, AclPermission.VIEWER);
        return this.fullBundleModelAssembler.toModel(
                bundleDAO
        ).add(
                linkTo(BundleController.class).withSelfRel()
        );
    }

    @GetMapping("/all")
    public CollectionModel<FullBundleDTO> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(name = "permission", defaultValue = "viewer") String permission
            ){
        this.logger.info("-----------");
        this.logger.info("Getting all bundles");
        this.logger.debug("page: " + page.toString());
        this.logger.debug("size: " + size.toString());
        this.logger.debug("permission: " + permission);
        List<BundleDAO> bundleDAOList = this.bundleService.findAll(
                PageRequest.of(page, size),
                AclPermission.valueOf(permission)
        );
        return this.fullBundleModelAssembler.toCollectionModel(
                bundleDAOList
        ).add(
                linkTo(BundleController.class).withSelfRel()
        );
    }

    @GetMapping("/food/items/{id}")
    public CollectionModel<FullFoodItemDTO> getFoodItems(
            @PathVariable("id") Long bundleId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "itemPermission", defaultValue = "viewer") String itemPermission
    ) throws ObjectNotFound {
        this.logger.info("------");
        this.logger.info("Returning all food items of bundle");
        BundleDAO bundleDAO = this.bundleService.findById(bundleId);
        this.logger.debug("bundle: " + bundleDAO.toString());
        this.logger.debug("item permission: " + itemPermission);
        this.bundleService.hasAccess(
                bundleDAO,
                AclPermission.VIEWER
        );
        List<FoodItemDAO> foodItemDAOS = this.foodItemService.findAll(
                PageRequest.of(page, size),
                AclPermission.valueOf(itemPermission)

        );
        foodItemDAOS = foodItemDAOS.stream()
                .filter(
                        a -> a.getBundleDAO().equals(bundleDAO)
                )
                .collect(Collectors.toList());
        this.logger.debug("food items: " + foodItemDAOS);
        this.logger.debug("food permission: " + itemPermission);
        return this.fullFoodItemModelAssembler.toCollectionModel(
                foodItemDAOS
        ).add(
                WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
        );
    }

    @PostMapping("")
    EntityModel<String> stringEntityModel(
            BundleDAO bundleDAO,
            BindingResult bindingResult) throws ObjectInvalid {
        this.logger.info("-----------");
        this.logger.info("Saving new bundle");
        this.logger.debug(bundleDAO.toString());
        if(bindingResult.hasErrors()) {
            this.logger.debug("Bundle has errors");
            throw new ObjectInvalid(
                    bundleDAO,
                    bindingResult,
                    WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
            );
        }
        bundleDAO = bundleRepository.save(bundleDAO);
        securityService.grantPermission(
                bundleDAO,
                AclPermission.EDITOR
        );
        this.logger.debug("Bundle saved");
        return new ConfirmationTemplate(
            ConfirmationTemplate.Token.ADD,
                BundleDAO.class.getName(),
                linkTo(BundleController.class).withRel("controller")
        ).getEntityModel();
    }

    @DeleteMapping("/{id}")
    public EntityModel<String> delete(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Deleting address");
        this.logger.debug("id is " + id.toString());
        BundleDAO bundleDAO = this.bundleService.findById(id);
        this.bundleService.hasAccess(bundleDAO, AclPermission.EDITOR);
        this.logger.debug("Bundle deleted");
        this.bundleRepository.delete(bundleDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.DELETE,
                BundleDAO.class.getName(),
                linkTo(
                        methodOn(BundleController.class).delete(id)
                ).withRel("controller")
        ).getEntityModel();
    }

    @PutMapping("/{id}")
    public EntityModel<String> put(
            @ModelAttribute BundleDAO bundleDAO,
            BindingResult bindingResult
    ) throws ObjectInvalid, ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Updating bundle");
        this.logger.debug(bundleDAO.toString());
        if(bindingResult.hasErrors()) {
            this.logger.debug("Bundle has errors");
            this.logger.debug(bindingResult.getAllErrors().toString());
            throw new ObjectInvalid(
                    bundleDAO,
                    bindingResult,
                    linkTo(BundleController.class).withSelfRel()
            );
        }
        this.bundleService.checkIfExists(bundleDAO.getId());
        this.bundleService.hasAccess(bundleDAO, AclPermission.EDITOR);
        this.logger.debug("Bundle updated");
        this.bundleRepository.save(bundleDAO);
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PUT,
                BundleDAO.class.getName(),
                linkTo(BundleController.class).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/add-editor/{id}")
    public EntityModel<String> addAsEditor(
            @PathVariable("id") Long bundleId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Adding user as editor to bundle");
        this.logger.debug("bundle: " + bundleId.toString());
        this.logger.debug("user: " + userId.toString());
        BundleDAO bundleDAO = this.bundleService.findById(bundleId);
        this.bundleService.hasAccess(bundleDAO, AclPermission.EDITOR);
        this.securityService.grantPermission(
                bundleDAO,
                AclPermission.EDITOR,
                this.userService.findById(userId)
        );
        this.logger.debug("User added");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                BundleDAO.class.getCanonicalName(),
                linkTo(
                        methodOn(BundleController.class).addAsEditor(bundleId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/remove-editor/{id}")
    public EntityModel<String> removeAsEditor(
            @PathVariable("id") Long bundleId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("removing user as editor of bundle");
        this.logger.debug("user: " + userId.toString());
        this.logger.debug("bundle: " + bundleId.toString());
        BundleDAO bundleDAO = this.bundleService.findById(bundleId);
        this.bundleService.hasAccess(
                bundleDAO,
                AclPermission.EDITOR
        );
        this.securityService.revokePermission(
                bundleDAO,
                AclPermission.EDITOR,
                this.userService.findById(userId)
        );
        this.logger.debug("User removed");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                BundleDAO.class.getCanonicalName(),
                linkTo(
                        methodOn(BundleController.class).removeAsEditor(bundleId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }


    @PatchMapping("/add-viewer/{id}")
    public EntityModel<String> addAsViewer(
            @PathVariable("id") Long bundleId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("adding user as viewer of bundle");
        this.logger.debug("user: " + userId.toString());
        this.logger.debug("bundle: " + bundleId.toString());
        BundleDAO bundleDAO = this.bundleService.findById(bundleId);
        this.bundleService.hasAccess(
                bundleDAO,
                AclPermission.EDITOR
        );
        this.securityService.grantPermission(
                bundleDAO,
                AclPermission.VIEWER,
                this.userService.findById(userId)
        );
        this.logger.debug("User added");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                BundleDAO.class.getCanonicalName(),
                linkTo(
                        methodOn(BundleController.class).addAsViewer(bundleId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @PatchMapping("/remove-viewer/{id}")
    public EntityModel<String> removeAsViewer(
            @PathVariable("id") Long bundleId,
            @RequestParam("user_id") Long userId
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("removing user as viewer of bundle");
        this.logger.debug("user: " + userId.toString());
        this.logger.debug("bundle: " + bundleId.toString());
        BundleDAO bundleDAO = this.bundleService.findById(bundleId);
        this.bundleService.hasAccess(
                bundleDAO,
                AclPermission.EDITOR
        );
        this.securityService.revokePermission(
                bundleDAO,
                AclPermission.VIEWER,
                this.userService.findById(userId)
        );
        this.logger.debug("User removed");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PATCH,
                BundleDAO.class.getCanonicalName(),
                linkTo(
                        methodOn(BundleController.class).removeAsViewer(bundleId, userId)
                ).withSelfRel()
        ).getEntityModel();
    }

    @GetMapping("/permissions/{id}")
    public CollectionModel<AclPermission> getPermissions(
            @PathVariable("id") Long id
    ) throws ObjectNotFound {
        BundleDAO bundleDAO = this.bundleService.findById(id);
        List<AclPermission> permissions = this.securityService.acquiredPermissions(bundleDAO);
        return CollectionModel.of(
                permissions
        ).add(
                linkTo(BundleController.class).withSelfRel()
        );
    }
}
