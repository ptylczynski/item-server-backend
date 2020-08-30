package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class ObjectNotFoundResolverProvider extends AbstractErrorResolverProvider {

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(ObjectNotFound.class);


    @Override
    public boolean canResolve(Class<?> exception) {
        return ObjectNotFound.class.isAssignableFrom(exception);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        this.logger.info("Object resolved as Object Not Found");
        ObjectNotFound ex = (ObjectNotFound) exception;
        ErrorTemplate errorJsonTemplate = ErrorTemplate.builder()
                .reason(
                        this.createMessage(ex)
                )
                .object(null).build();
        this.logger.debug("message: " + this.createMessage(ex));
        return EntityModel.of(errorJsonTemplate, ex.getLink());
    }

    private String createMessage(ObjectNotFound exception){
        return this.messageSource.getMessage(
                "object.not.found",
                new Object[]{exception.getDiscriminator()},
                "Missing reason",
                LocaleContextHolder.getLocale()
        );
    }
}
