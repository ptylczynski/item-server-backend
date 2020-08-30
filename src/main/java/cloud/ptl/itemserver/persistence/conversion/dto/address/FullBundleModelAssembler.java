package cloud.ptl.itemserver.persistence.conversion.dto.address;

import cloud.ptl.itemserver.persistence.conversion.dto.user.UserCensoredModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class FullBundleModelAssembler extends RepresentationModelAssemblerSupport<BundleDAO, FullBundleDTO> {

    @Autowired
    private UserCensoredModelAssembler userCensoredModelAssembler;

    public FullBundleModelAssembler() {
        super(BundleDAO.class, FullBundleDTO.class);
    }

    @Override
    public FullBundleDTO toModel(BundleDAO entity) {
        return FullBundleDTO.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .editors(this.getUsers(entity.getEditors()))
                .viewers(this.getUsers(entity.getViewers()))
                .owner(this.getUser(entity.getOwner()))
                .build();
    }

    private Set<UserCensoredDTO> getUsers(Set<UserDAO> users){
        Set<UserCensoredDTO> result = new HashSet<>();
        for(UserDAO user : users){
            result.add(
                    this.userCensoredModelAssembler.toModel(user)
            );
        }
        return result;
    }

    private UserCensoredDTO getUser(UserDAO userDAO){
        return this.userCensoredModelAssembler.toModel(userDAO);
    }
}
