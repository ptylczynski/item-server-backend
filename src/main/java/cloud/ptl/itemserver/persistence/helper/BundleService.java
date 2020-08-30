package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.controllers.BundleController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.validation.UserAlreadyAddedToBundle;
import cloud.ptl.itemserver.error.exception.validation.UserNotAddedToBundle;
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

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(BundleService.class);

    public Boolean checkifBundleExists(Long id) throws ObjectNotFound {
        this.logger.info("Checking if bundle id=" + id + " exists");
        if(!this.bundleRepository.existsById(id)){
            this.logger.debug("Bundle does not exist");
            throw new ObjectNotFound(
                    BundleDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
            );
        }
        this.logger.debug("Bundle exists");
        return true;
    }

    public void checkIfUserIsAdded(BundleDAO bundle, UserDAO user, UserRole userRole) throws UserAlreadyAddedToBundle {
        this.logger.info("Checking if user is added to bundle");
        this.logger.debug("userId: " + user.getId());
        this.logger.debug("bundle: " + bundle.getId());
        this.logger.debug("userRole: " + userRole.toString());
        if(this.userAlreadyIs(bundle, user).contains(userRole)){
            throw new UserAlreadyAddedToBundle(
                    bundle,
                    user,
                    WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
            );
        }
    }

    public void checkIfUserIsNotAdded(BundleDAO bundleDAO, UserDAO userDAO, UserRole userRole) throws UserNotAddedToBundle {
        this.logger.info("Checking if user is not added to bundle");
        this.logger.debug("userId: " + userDAO.getId());
        this.logger.debug("bundle: " + bundleDAO.getId());
        this.logger.debug("userRole: " + userRole.toString());
        if(!this.userAlreadyIs(bundleDAO, userDAO).contains(userRole)){
            throw new UserNotAddedToBundle(
                    WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel(),
                    bundleDAO
            );
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

    public BundleDAO findById(Long id) throws ObjectNotFound {
        this.logger.info("Searching bundle");
        this.logger.debug("id=" + id);
        this.checkifBundleExists(id);
        return this.bundleRepository.findById(id).get();
    }

    public void addUserAsEditor(Long userId, Long bundleId) throws ObjectNotFound, UserAlreadyAddedToBundle {
        this.logger.info("Adding user");
        UserDAO userDAO = this.userService.findById(userId);
        BundleDAO bundleDAO = this.findById(bundleId);
        this.checkIfUserIsAdded(bundleDAO, userDAO, UserRole.EDITOR);
        bundleDAO.getEditors().add(userDAO);
        this.bundleRepository.save(bundleDAO);
    }

    public void removeUserAsEditor(Long userId, Long bundleId) throws ObjectNotFound, UserNotAddedToBundle {
        this.logger.info("Removing user");
        UserDAO userDAO = this.userService.findById(userId);
        BundleDAO bundleDAO = this.findById(bundleId);
        this.checkIfUserIsNotAdded(bundleDAO, userDAO, UserRole.EDITOR);
        bundleDAO.getEditors().remove(userDAO);
        this.bundleRepository.save(bundleDAO);
    }

    public void addUserAsViewer(Long userId, Long bundleId) throws ObjectNotFound, UserAlreadyAddedToBundle {
        this.logger.info("Adding user");
        UserDAO userDAO = this.userService.findById(userId);
        BundleDAO bundleDAO = this.findById(bundleId);
        this.checkIfUserIsAdded(bundleDAO, userDAO, UserRole.VIEWER);
        bundleDAO.getViewers().add(userDAO);
        this.bundleRepository.save(bundleDAO);
    }

    public void removeUserAsViewer(Long userId, Long bundleId) throws ObjectNotFound, UserNotAddedToBundle {
        this.logger.info("Removing user");
        UserDAO userDAO = this.userService.findById(userId);
        BundleDAO bundleDAO = this.findById(bundleId);
        this.checkIfUserIsNotAdded(bundleDAO, userDAO, UserRole.VIEWER);
        bundleDAO.getViewers().remove(userDAO);
        this.bundleRepository.save(bundleDAO);
    }

    public enum UserRole{
        VIEWER, EDITOR, OWNER, NOT_ADDED
    }
}
