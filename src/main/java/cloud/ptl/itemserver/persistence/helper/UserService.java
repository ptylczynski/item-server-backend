package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.controllers.UserController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Boolean checkIfUserExist(Long id) throws ObjectNotFound {
        this.logger.info("Checking if user exists");
        if(!this.userRepository.existsById(id)){
            this.logger.debug("User does not exist");
            throw new ObjectNotFound(
                    id,
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        }
        this.logger.debug("User exists");
        return true;
    }

    public Boolean checkIfUserExist(String designator) throws ObjectNotFound {
        this.logger.info("Checking if user exist");
        this.logger.debug("Designator=" + designator);
        ObjectNotFound error = new ObjectNotFound(
                designator,
                WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
        );
        if(designator.contains("@")){
            this.logger.debug("Designator is mail");
            // designator is mail address
            if(!this.userRepository.existsByMail(designator)){
                this.logger.debug("User not found");
                throw error;
            }
        }
        else{
            // designator is username
            this.logger.debug("Designator is username");
            if(!this.userRepository.existsByUsername(designator)){
                this.logger.debug("User not found");
                throw error;
            }
        }
        this.logger.debug("User found");
        return true;
    }

    public UserDAO findById(Long id) throws ObjectNotFound {
        this.logger.info("Searching user");
        this.logger.debug("id=" + id);
        this.checkIfUserExist(id);
        return this.userRepository.findById(id).get();
    }
}
