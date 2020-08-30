package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.parsing.ObjectUnformatable;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.helper.UserService;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Component
public class UserEditor extends PropertyEditorSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserEditor.class);

    @Override
    public String getAsText() {
        this.logger.info("Transforming UserDAO into text");
        UserDAO userDAO = (UserDAO) this.getValue();
        return userDAO.getId().toString();
    }

    @Override
    public void setAsText(String text) throws ObjectUnformatable {
        try {
            this.logger.info("Transforming text into UserDAO");
            this.logger.debug("text=" + text);
            Long id = Long.valueOf(text);
            this.userService.checkIfUserExist(id);
            Optional<UserDAO> userDAO = this.userRepository.findById(id);
            this.logger.debug("Transformed into: " + userDAO.get().toString());
            this.setValue(userDAO.get());
        } catch (ObjectNotFound objectNotFound) {
            objectNotFound.printStackTrace();
            throw new IllegalArgumentException(text + " is unformatable");
        }
    }
}
