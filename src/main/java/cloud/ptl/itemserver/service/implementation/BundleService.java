package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.controllers.BundleController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.error.exception.permission.InsufficientPermission;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.address.FullBundleModelAssembler;
import cloud.ptl.itemserver.persistence.dao.authorization.AclEntryDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.repositories.authorization.AclEntryRepository;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BundleService {

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private GeneralService generalService;

    @Autowired
    private UserService userService;

    @Autowired
    private AclEntryRepository aclEntryRepository;

    @Autowired
    private BundleService bundleService;

    @Autowired
    private FoodTypeService foodTypeService;

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
        BundleDAO bundleDAO = this.findById(fullBundleDTO.getId());
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

    public BundleDAO findById(Long id) throws ObjectNotFound {
        this.logger.info("Searching bundle");
        this.logger.debug("id=" + id);
        this.checkifBundleExists(id);
        return this.bundleRepository.findById(id).get();
    }

    public List<BundleDAO> findAll(Pageable pageable, AclPermission permission){
        this.logger.info("Fetching all bundles accessiable by user");
        List<AclEntryDAO> aclEntryDAOS =
                this.securityService.getAllAccesiableAclEntries(
                        BundleDAO.class,
                        pageable
                );
        List<BundleDAO> bundleDAOS =
                this.bundleRepository.findAllById(
                aclEntryDAOS.stream()
                        // filter for only acl entries with given or higher permission
                        .filter(a -> a.getAclPermissions().stream()
                                .anyMatch(permission::sameOrLower))
                        // extract ids
                        .map(a -> a.getAclIdentityDAO().getObjectId())
                        .collect(Collectors.toSet())
        );
        this.logger.debug("acl entries: " + aclEntryDAOS.toString());
        this.logger.debug("bundles: " + bundleDAOS.toString());
        return bundleDAOS;
    }

    public Boolean hasAccess(BundleDAO bundleDAO, AclPermission permission){
        if(this.securityService.hasPermission(bundleDAO, permission)) return true;
        else throw new InsufficientPermission(
                FoodItemDAO.class.getCanonicalName(),
                permission,
                WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
        );
    }
}
