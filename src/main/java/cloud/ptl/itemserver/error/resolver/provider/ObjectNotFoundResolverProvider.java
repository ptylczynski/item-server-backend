package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.error.exception.item.ObjectNotFound;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class ObjectNotFoundResolverProvider extends AbstractErrorResolverProvider {

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean canResolve(Class<?> exception) {
        return ObjectNotFound.class.isAssignableFrom(exception);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        ObjectNotFound ex = (ObjectNotFound) exception;
        ErrorTemplate errorJsonTemplate = ErrorTemplate.builder()
                .reason(
                        this.createMessage(ex)
                )
                .object(null).build();
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
