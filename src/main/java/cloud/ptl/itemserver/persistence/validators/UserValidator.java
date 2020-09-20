package cloud.ptl.itemserver.persistence.validators;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(UserValidator.class);

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UserDAO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if(o.getClass().isAssignableFrom(UserDAO.class)){
            UserDAO userDAO = (UserDAO) o;
            this.logger.info("Validating user");
            this.logger.debug("user: " + userDAO.toString());

            // check if username exists
            if(userDAO.getUsername() == null){
                this.logger.debug("username is null");
                errors.rejectValue("username", "missing.username");
            }

            // check if password exists
            if(userDAO.getPassword() == null){
                this.logger.debug("password is empty");
                errors.rejectValue("password", "empty.password");
            }

            // check if email exists
            if(userDAO.getMail() == null){
                this.logger.debug("mail is empty");
                errors.rejectValue("mail", "empty.mail");
            }

            // check if display name exists
            if(userDAO.getDisplayName() == null){
                this.logger.debug("display name is empty");
                errors.rejectValue("displayname", "empty.displayname");
            }
        }
    }
}
