package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.error.exception.parsing.ObjectInvalid;
import cloud.ptl.itemserver.error.resolver.BindingResultToStringTransformer;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class ObjectInvalidResolverProvider extends AbstractErrorResolverProvider{
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BindingResultToStringTransformer bindingResultToStringTransformer;

    private final Logger logger = LoggerFactory.getLogger(ObjectInvalid.class);

    @Override
    public boolean canResolve(Class<?> exception) {
        return ObjectInvalid.class.isAssignableFrom(exception);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        this.logger.info("Error resolved as Object Invalid");
        ObjectInvalid ex = (ObjectInvalid) exception;
        ErrorTemplate errorJsonTemplate = ErrorTemplate.builder()
                .reason(
                        this.bindingResultToStringTransformer.transform(
                                ex.getBindingResult()
                        )
                )
                .object(ex.getObject()).build();
        return EntityModel.of(errorJsonTemplate, ex.getLink());
    }
}
