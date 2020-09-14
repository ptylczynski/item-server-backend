package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.controllers.BundleController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.address.FullBundleModelAssembler;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

@Service
public class BundleService {

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

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

    public BundleDAO toBundleDAO(FullBundleDTO fullBundleDTO) throws ObjectNotFound {
        this.logger.info("Converting Full Bundle DTO to Bundle DAO");
        BundleDAO bundleDAO = this.findById(fullBundleDTO.getId(), BundleDAO.class);
        bundleDAO.setDescription(
                fullBundleDTO.getDescription()
        );
        bundleDAO.setName(
                fullBundleDTO.getName()
        );
        return bundleDAO;
    }

    public void save(BundleDAO bundleDAO){
        this.logger.info("Saving bundle to db");
        this.bundleRepository.save(bundleDAO);
    }

    public void save(FullBundleDTO fullBundleDTO) throws ObjectNotFound {
        this.bundleRepository.save(
                this.toBundleDAO(fullBundleDTO)
        );
    }

    public <T> T findById(Long id, Class<T> clazz) throws ObjectNotFound {
        this.logger.info("Searching bundle");
        this.logger.debug("id=" + id);
        this.checkifBundleExists(id);
        BundleDAO bundleDAO =
                this.bundleRepository.findById(id).get();
        if(clazz.isAssignableFrom(FullBundleDTO.class)){
            return clazz.cast(this.fullBundleModelAssembler.toModel(bundleDAO));
        }
        else if (clazz.isAssignableFrom(BundleDAO.class)){
            return clazz.cast(bundleDAO);
        }
        else{
            throw new IllegalArgumentException(id.toString());
        }
    }

}
