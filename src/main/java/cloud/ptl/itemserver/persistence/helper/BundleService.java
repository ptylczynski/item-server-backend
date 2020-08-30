package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.controllers.BundleController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.validation.UserAlreadyAddedToBundle;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BundleService {

    @Autowired
    private BundleRepository bundleRepository;

    private final Logger logger = LoggerFactory.getLogger(BundleService.class);

    public Boolean checkifBundleExists(Long id) throws ObjectNotFound {
        this.logger.info("Checking if bundle id=" + id + " exists");
        if(!this.bundleRepository.existsById(id)){
            this.logger.debug("Bundle does not exist");
            throw new ObjectNotFound(
                    id,
                    WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
            );
        }
        this.logger.debug("Bundle exists");
        return true;
    }

    public void checkIfUserIsAdded(BundleDAO bundle, UserDAO user) throws UserAlreadyAddedToBundle {
        if(!this.userAlreadyIs(bundle, user).contains(UserRole.NOT_ADDED)){
            throw new UserAlreadyAddedToBundle(
                    bundle,
                    user,
                    WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
            );
        }
    }

    public void checkIfUserIsNotAdded(BundleDAO bundleDAO, UserDAO userDAO){
        if(this.userAlreadyIs(bundleDAO, userDAO).contains(UserRole.NOT_ADDED)){

        }
    }

    public List<UserRole> userAlreadyIs(BundleDAO bundleDAO, UserDAO userDAO){
        List<UserRole> roles = new ArrayList<>();
        if(bundleDAO.getEditors().contains(userDAO)) roles.add(UserRole.EDITOR);
        if(bundleDAO.getViewers().contains(userDAO)) roles.add(UserRole.VIEWER);
        if(bundleDAO.getOwner().equals(userDAO)) roles.add(UserRole.OWNER);
        if(roles.size() == 0) roles.add(UserRole.NOT_ADDED);
        return roles;
    }

    public enum UserRole{
        VIEWER, EDITOR, OWNER, NOT_ADDED
    }
}
