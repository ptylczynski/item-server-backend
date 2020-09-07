package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.persistence.conversion.dto_assembler.address.FullBundleModelAssembler;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Component
public class BundleDAOEditor extends PropertyEditorSupport {

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

    private final Logger logger = LoggerFactory.getLogger(BundleDAOEditor.class);

    @Override
    public String getAsText() {
        this.logger.info("Transforming BundleDTO into text");
        BundleDAO bundleDAO = (BundleDAO) this.getValue();
        return String.valueOf(bundleDAO.getId());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.logger.info("Transforming text into BundleDTO");
        this.logger.debug("text: " + text);
        Optional<BundleDAO> bundleDAO = bundleRepository.findById(
                Long.valueOf(text)
        );
        if(bundleDAO.isEmpty()) {
            this.logger.debug("Object unformatable");
            this.logger.debug("Object: " + text);
            throw new IllegalArgumentException(text);
        }
        this.setValue(bundleDAO.get());
    }
}
