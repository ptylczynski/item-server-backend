package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.error.exception.parsing.ObjectUnformatable;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;

public class ObjectUnformatableResolverProvider extends AbstractErrorResolverProvider {

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(ObjectUnformatableResolverProvider.class);

    @Override
    public boolean canResolve(Class<?> exception) {
        return exception.isAssignableFrom(ObjectUnformatable.class);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        ObjectUnformatable ex = (ObjectUnformatable) exception;
        this.logger.info("Error resolved as Object Unformatable");
        ErrorTemplate errorTemplate = ErrorTemplate.builder()
                .object(null)
                .reason(
                        this.messageSource.getMessage(
                                ex.getMessage(),
                                new Object[]{},
                                LocaleContextHolder.getLocale()
                        )
                )
                .build();
        return EntityModel.of(
                errorTemplate,
                ex.getLink()
        );
    }
}
