package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.springframework.hateoas.EntityModel;

public abstract class AbstractErrorResolverProvider {
    public abstract boolean canResolve(Class<?> exception);
    public abstract <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception);
}
