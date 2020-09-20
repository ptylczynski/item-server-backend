package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.error.exception.permission.InsufficientPermission;
import cloud.ptl.itemserver.error.resolver.transformers.ClassNameToStringTransformer;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import javax.mail.Message;

@Service
public class InsufficientPermissionResolverProvider extends AbstractErrorResolverProvider{

    private final Logger logger = LoggerFactory.getLogger(InsufficientPermissionResolverProvider.class);

    // TODO: resolve method

    @Autowired
    private ClassNameToStringTransformer classNameToStringTransformer;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean canResolve(Class<?> exception) {
        return InsufficientPermission.class.isAssignableFrom(exception);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        this.logger.info("Error resolved as Insufficient permission");
        InsufficientPermission ex = (InsufficientPermission) exception;
        ErrorTemplate errorTemplate = ErrorTemplate.builder()
                .reason(
                        this.getMessage(ex)
                )
                .object(null)
                .build();
        return EntityModel.of(
                errorTemplate
        ).add(
                ex.getLink()
        );
    }

    public String getMessage(InsufficientPermission ex){
        return this.messageSource.getMessage(
                "insufficient.permission",
                new Object[]{
                        this.classNameToStringTransformer.transform(ex.getClazz()),
                        ex.getRequiredPermission().toString()
                },
                "Missing description",
                LocaleContextHolder.getLocale()
        );
    }
}
