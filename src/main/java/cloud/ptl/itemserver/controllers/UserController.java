package cloud.ptl.itemserver.controllers;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
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

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public EntityModel<UserCensored> getOne(
            @PathVariable Long id
    ) throws ObjectNotFound {
        this.logger.info("Getting info about user");
        this.logger.debug("Id is: " + id);
        Optional<UserCensored> userDAO = this.userRepository.findById(id, UserCensored.class);
        if(userDAO.isEmpty()){
            this.logger.debug("User with id: " + id + " not found");
            throw new ObjectNotFound(
                    id,
                    WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(UserController.class).getOne(id)
                    ).withSelfRel()
            );
        }
        this.logger.debug("User found");
        this.logger.debug(userDAO.get().toString());
        return EntityModel.of(
                userDAO.get(),
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getOne(id)
                ).withSelfRel()
        );
    }

    @GetMapping("/all")
    public CollectionModel<UserCensored> getAll(){
        ArrayList<UserCensored> users = this.userRepository.findBy(UserCensored.class);
        return CollectionModel.of(
                users,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getAll()
                ).withSelfRel()
        );
    }
}
