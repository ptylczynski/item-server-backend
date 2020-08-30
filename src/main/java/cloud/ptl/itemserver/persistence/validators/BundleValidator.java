package cloud.ptl.itemserver.persistence.validators;


import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

public class BundleValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(BundleDAO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if(!o.getClass().isAssignableFrom(BundleDAO.class)) return;
        else{
            BundleDAO bundleDAO = (BundleDAO) o;

            // check if owner is valid
            if(!this.userRepository.existsById(bundleDAO.getOwner().getId())){
                errors.rejectValue("owner", "owner.does.not.exist");
            }

            // check if editors exist
            if(!this.checkIfUsersExist(bundleDAO.getEditors())){
                errors.rejectValue("editors", "editor.does.not.exist");
            }

            // check if viewers exist
            if(!this.checkIfUsersExist(bundleDAO.getViewers())){
                errors.rejectValue("viewers", "viewer.does.not.exist");
            }
        }
    }

    private Boolean checkIfUsersExist(Set<UserDAO> users){
        for(UserDAO user : users){
            Long id = user.getId();
            if(!this.userRepository.existsById(id)){
                return false;
            }
        }
        return true;
    }
}
