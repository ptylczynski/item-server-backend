package cloud.ptl.itemserver.persistence.conversion.dto.user;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.projections.userDAO.UserCensored;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserCensoredModelAssembler extends RepresentationModelAssemblerSupport<UserDAO, UserCensoredDTO> {
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

    public UserCensoredDTO toModel(UserCensored entity){
        return UserCensoredDTO.builder()
                .username(entity.getUsername())
                .mail(entity.getMail())
                .id(entity.getId())
                .displayName(entity.getDisplayName())
                .build();
    }
}
