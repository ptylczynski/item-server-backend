package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.user.UserCensoredModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.projections.userDAO.UserCensored;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import cloud.ptl.itemserver.service.implementation.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

    @GetMapping("/{id}")
    public UserCensoredDTO getOne(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("Getting info about user");
        this.logger.debug("Id is: " + id);
        this.userService.checkIfUserExist(id);
        Optional<UserCensored> userDAO = this.userRepository.findById(id, UserCensored.class);
        this.logger.debug(userDAO.get().toString());
        return this.userCensoredModelAssembler
                .toModel(userDAO.get())
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
        this.userService.checkIfUserExist(username);
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
                        PageRequest.of(page, size)
                );
        return this.userCensoredModelAssembler
                .toCollectionModel(userDAOS)
                .add(
                    WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getAll(page, size)
                    ).withSelfRel()
                );
    }
}
