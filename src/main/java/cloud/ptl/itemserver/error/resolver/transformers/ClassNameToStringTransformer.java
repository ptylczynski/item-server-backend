package cloud.ptl.itemserver.error.resolver.transformers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ClassNameToStringTransformer extends AbstractTransformer<String, String> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String transform(String value) {
        return this.messageSource.getMessage(
                value,
                new Object[]{},
                " ? ",
                LocaleContextHolder.getLocale()
        );
    }
}
