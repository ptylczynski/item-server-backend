package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.user.UserCensoredModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.projections.userDAO.UserCensored;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import cloud.ptl.itemserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

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
        Optional<UserDAO> userDAO = this.userRepository.findByUsername(username);
        return this.userCensoredModelAssembler
                .toModel(userDAO.get())
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
        this.userService.checkIfUserExist(mail);
        Optional<UserDAO> userDAO = this.userRepository.findByMail(mail);
        return this.userCensoredModelAssembler
                .toModel(userDAO.get())
                .add(
                        WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(UserController.class).infoByMail(mail)
                        ).withSelfRel()
                );
    }

    @GetMapping("/all")
    public CollectionModel<UserCensoredDTO> getAll(){
        this.logger.info("-----------");
        Iterable<UserDAO> users = this.userRepository.findBy(UserDAO.class);
        return this.userCensoredModelAssembler
                .toCollectionModel(users)
                .add(
                    WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getAll()
                    ).withSelfRel()
                );
    }
}
