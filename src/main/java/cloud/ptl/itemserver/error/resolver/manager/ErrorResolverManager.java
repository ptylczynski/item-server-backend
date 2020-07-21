package cloud.ptl.itemserver.error.resolver.manager;

import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.springframework.hateoas.EntityModel;

public abstract class ErrorResolverManager {
    public abstract <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception);
}
