package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.controllers.UserController;
import cloud.ptl.itemserver.error.exception.parsing.ObjectUnformatable;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.user.UserCensoredModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.helper.service.UserService;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Component
public class UserEditor extends PropertyEditorSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCensoredModelAssembler userCensoredModelAssembler;

    private final Logger logger = LoggerFactory.getLogger(UserEditor.class);

    @Override
    public String getAsText() {
        this.logger.info("Transforming UserDTO into text");
        UserCensoredDTO userCensoredDTO = (UserCensoredDTO) this.getValue();
        return userCensoredDTO.getId().toString();
    }

    @Override
    public void setAsText(String text) throws ObjectUnformatable {
        this.logger.info("Transforming object into UserDTO");
        this.logger.debug("object: " + text);
        Optional<UserDAO> userDAO = this.userRepository.findById(
                Long.valueOf(text)
        );
        if(userDAO.isEmpty()){
            this.logger.debug("Object unformatable");
            throw new ObjectUnformatable(
                    WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()
            );
        }
        this.setValue(userDAO.get());
    }
}
