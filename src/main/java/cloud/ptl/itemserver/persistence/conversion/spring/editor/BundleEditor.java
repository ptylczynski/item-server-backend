package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Component
public class BundleEditor extends PropertyEditorSupport {

    @Autowired
    private BundleRepository bundleRepository;

    private final Logger logger = LoggerFactory.getLogger(BundleEditor.class);

    @Override
    public String getAsText() {
        this.logger.info("Transforming BundleDAO into text");
        BundleDAO bundleDAO = (BundleDAO) this.getValue();
        Long id = bundleDAO.getId();
        return String.valueOf(id);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.logger.info("Transforming text into BundleDAO");
        this.logger.debug("text: " + text);
        Optional<BundleDAO> bundleDAO = bundleRepository.findById(
                Long.valueOf(text)
        );
        if(bundleDAO.isEmpty()) throw new IllegalArgumentException(
                String.format("Bundle %s unformatable", text)
        );
        this.logger.debug("Transformed into: " + bundleDAO.get().toString());
        this.setValue(bundleDAO.get());
    }
}
