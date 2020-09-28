package cloud.ptl.itemserver.persistence.conversion.spring.editor;

import cloud.ptl.itemserver.persistence.dao.i18n.LocaleDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.beans.PropertyEditorSupport;
import java.util.Locale;

@Service
public class LocaleDAOEditor extends PropertyEditorSupport {

    private final Logger logger = LoggerFactory.getLogger(LocaleDAOEditor.class);

    @Override
    public String getAsText() {
        this.logger.info("Transforming locale into text");
        LocaleDAO localeDAO = (LocaleDAO) this.getValue();
        this.logger.debug("locale: " + localeDAO.toString());
        return localeDAO.getLanguage();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.logger.info("Transforming text into locale");
        this.logger.debug("text: " + text);
        Locale locale = Locale.forLanguageTag(text);
        this.setValue(locale);
    }
}
