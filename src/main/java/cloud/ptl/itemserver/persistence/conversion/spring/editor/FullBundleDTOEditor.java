package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.persistence.conversion.dto_assembler.address.FullBundleModelAssembler;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import cloud.ptl.itemserver.persistence.dto.address.FullBundleDTO;
import cloud.ptl.itemserver.persistence.repositories.bundle.BundleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

public class FullBundleDTOEditor extends PropertyEditorSupport {

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

    private final Logger logger = LoggerFactory.getLogger(FullBundleDTOEditor.class);

    @Override
    public String getAsText() {
        this.logger.info("Transforming Full Bundle DTO into text");
        FullBundleDTO fullBundleDTO = (FullBundleDTO) this.getValue();
        this.logger.debug(fullBundleDTO.toString());
        return super.getAsText();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.logger.info("Transforming string into Full Bundle DTO");
        this.logger.debug("Text: " + text);
        Optional<BundleDAO> bundleDAO =
                this.bundleRepository.findById(
                        Long.valueOf(text)
                );
        if(bundleDAO.isEmpty()){
            this.logger.debug("Object " + text + " unformatable");
            throw new IllegalArgumentException(text);
        }
        super.setValue(
                this.fullBundleModelAssembler.toModel(bundleDAO.get())
        );
    }
}
