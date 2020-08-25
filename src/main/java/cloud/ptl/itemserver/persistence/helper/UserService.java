package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
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
                    WebMvcLinkBuilder.linkTo(null).withSelfRel()
            );
        }
        this.logger.debug("User exists");
        return true;
    }
}
