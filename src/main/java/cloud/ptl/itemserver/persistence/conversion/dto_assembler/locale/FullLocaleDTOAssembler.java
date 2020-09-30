package cloud.ptl.itemserver.persistence.conversion.dto_assembler.locale;

import cloud.ptl.itemserver.persistence.dao.i18n.LocaleDAO;
import cloud.ptl.itemserver.persistence.dto.locale.FullLocaleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class FullLocaleDTOAssembler extends RepresentationModelAssemblerSupport<LocaleDAO, FullLocaleDTO> {

    private final Logger logger = LoggerFactory.getLogger(FullLocaleDTOAssembler.class);

    public FullLocaleDTOAssembler() {
        super(LocaleDAO.class, FullLocaleDTO.class);
    }

    @Override
    public FullLocaleDTO toModel(LocaleDAO entity) {
        this.logger.info("Transforming Locale DAO into locale DTO");
        return FullLocaleDTO.builder()
                .country(entity.getCountry())
                .language(entity.getLanguage())
                .build();
    }
}
