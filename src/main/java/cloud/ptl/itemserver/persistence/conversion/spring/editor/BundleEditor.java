package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.controllers.BundleController;
import cloud.ptl.itemserver.error.exception.parsing.ObjectUnformatable;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.address.FullBundleModelAssembler;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Component
public class BundleEditor extends PropertyEditorSupport {

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

    private final Logger logger = LoggerFactory.getLogger(BundleEditor.class);

    @Override
    public String getAsText() {
        this.logger.info("Transforming BundleDTO into text");
        FullBundleDTO fullBundleDTO = (FullBundleDTO) this.getValue();
        return String.valueOf(fullBundleDTO.getId());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.logger.info("Transforming text into BundleDTO");
        this.logger.debug("text: " + text);
        // manual fetching to use ObjectUnformatable error instead of ObjectNotFound
        // provided by bundle service
        Optional<BundleDAO> bundleDAO = bundleRepository.findById(
                Long.valueOf(text)
        );
        if(bundleDAO.isEmpty()) {
            this.logger.debug("Object unformatable");
            this.logger.debug("Object: " + text);
            throw new ObjectUnformatable(
                    WebMvcLinkBuilder.linkTo(BundleController.class).withSelfRel()
            );
        }
        this.setValue(bundleDAO.get());
    }
}
