package cloud.ptl.itemserver.persistence.conversion.dto_assembler.address;

import cloud.ptl.itemserver.persistence.conversion.dto_assembler.user.UserCensoredModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.dto.user.UserCensoredDTO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import cloud.ptl.itemserver.service.implementation.BundleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FullBundleModelAssembler extends RepresentationModelAssemblerSupport<BundleDAO, FullBundleDTO> {

    @Autowired
    private UserCensoredModelAssembler userCensoredModelAssembler;

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private BundleService bundleService;

    public FullBundleModelAssembler() {
        super(BundleDAO.class, FullBundleDTO.class);
    }

    @Override
    public FullBundleDTO toModel(BundleDAO entity) {
        return FullBundleDTO.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .name(entity.getName())
                .build();
    }

    private UserCensoredDTO getUser(UserDAO userDAO){
        return this.userCensoredModelAssembler.toModel(userDAO);
    }
}
