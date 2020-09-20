package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.user.UserCensoredModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.projections.userDAO.UserCensored;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCensoredModelAssembler userCensoredModelAssembler;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/{id}")
    public UserCensoredDTO getOne(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("Getting info about user");
        this.logger.debug("Id is: " + id);
        UserDAO userDAO = this.userService.findById(id);
        this.logger.debug(userDAO.toString());
        return this.userCensoredModelAssembler
                .toModel(userDAO)
                .add(
                    WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getOne(id)
                    ).withSelfRel()
                );
    }

    @GetMapping("/info/byUsername")
    public UserCensoredDTO infoByUsername(
            @RequestParam("username") String username
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Getting info about user");
        this.logger.debug("username=" + username);
        UserDAO userDAO = this.userService.findByUsername(username);
        return this.userCensoredModelAssembler
                .toModel(userDAO)
                .add(
                        WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(UserController.class).infoByUsername(username)
                        ).withSelfRel()
                );
    }

    @GetMapping("/info/byMail")
    public UserCensoredDTO infoByMail(
            @RequestParam("mail") String mail
    ) throws ObjectNotFound {
        this.logger.info("-----------");
        this.logger.info("Getting info about user");
        this.logger.debug("mail=" + mail);
        UserDAO userDAO = this.userService.findByMail(mail);
        return this.userCensoredModelAssembler
                .toModel(userDAO)
                .add(
                        WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(UserController.class).infoByMail(mail)
                        ).withSelfRel()
                );
    }

    @GetMapping("/all")
    public CollectionModel<UserCensoredDTO> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        this.logger.info("-----------");
        List<UserDAO> userDAOS =
                this.userService.findAll(
                        PageRequest.of(page, size),
                        AclPermission.VIEWER
                );
        return this.userCensoredModelAssembler
                .toCollectionModel(userDAOS)
                .add(
                    WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getAll(page, size)
                    ).withSelfRel()
                );
    }

    @PostMapping("")
    public EntityModel<String> post(
            @Validated UserDAO userDAO,
            BindingResult bindingResult
    ) throws ObjectInvalid {
        this.logger.info("-----------");
        this.logger.info("Registering user");
        this.logger.debug("user: " + userDAO.toString());
        this.logger.debug("binding result: " + bindingResult.toString());
        if(bindingResult.hasErrors()){
            this.logger.debug("binding result has errors");
            throw new ObjectInvalid(
                    userDAO,
                    bindingResult,
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        }
        this.userService.registerUser(userDAO);
        this.logger.debug("User registered");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.ADD,
                UserDAO.class.getCanonicalName(),
                WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
        ).getEntityModel();
    }

    @PutMapping("/{id}")
    public EntityModel<String> put(
            @Validated UserDAO userDAO,
            BindingResult bindingResult,
            @PathVariable("id") Long userId
    ) throws ObjectNotFound, ObjectInvalid {
        this.logger.info("-----------");
        this.logger.debug("Updating user info");
        this.logger.debug("updated user: " + userDAO.toString());
        if(bindingResult.hasErrors()){
            this.logger.debug("updated user has errors");
            this.logger.debug(bindingResult.toString());
            throw new ObjectInvalid(
                    userDAO,
                    bindingResult,
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        }
        this.userService.updateUser(userDAO);
        this.logger.debug("User updated");
        return new ConfirmationTemplate(
                ConfirmationTemplate.Token.PUT,
                UserDAO.class.getCanonicalName(),
                WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
        ).getEntityModel();
    }
}
