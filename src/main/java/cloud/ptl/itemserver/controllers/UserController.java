package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.conversion.dto.user.UserCensoredModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.helper.UserService;
import cloud.ptl.itemserver.persistence.projections.userDAO.UserCensored;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/all")
    public CollectionModel<UserCensoredDTO> getAll(){
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
