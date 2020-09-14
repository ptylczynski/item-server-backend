package cloud.ptl.itemserver.persistence.conversion.dto_assembler.user;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.projections.userDAO.UserCensored;
import cloud.ptl.itemserver.persistence.repositories.security.UserRepository;
import cloud.ptl.itemserver.service.implementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserCensoredModelAssembler extends RepresentationModelAssemblerSupport<UserDAO, UserCensoredDTO> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public UserCensoredModelAssembler() {
        super(UserDAO.class, UserCensoredDTO.class);
    }

    @Override
    public UserCensoredDTO toModel(UserDAO entity) {
        return UserCensoredDTO.builder()
                .displayName(entity.getDisplayName())
                .id(entity.getId())
                .mail(entity.getMail())
                .username(entity.getUsername())
                .build();
    }

    public UserCensoredDTO toModel(Long id) throws ObjectNotFound {
        return this.toModel(
                this.userService.findById(id)
        );
    }

    public UserCensoredDTO toModel(UserCensored entity){
        return UserCensoredDTO.builder()
                .username(entity.getUsername())
                .mail(entity.getMail())
                .id(entity.getId())
                .displayName(entity.getDisplayName())
                .build();
    }
}
