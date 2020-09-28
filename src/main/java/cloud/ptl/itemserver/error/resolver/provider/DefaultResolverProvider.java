package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.controllers.MainController;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class DefaultResolverProvider extends AbstractErrorResolverProvider {
    @Override
    public boolean canResolve(Class<?> exception) {
        return true;
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        ErrorTemplate errorTemplate = ErrorTemplate.builder()
                .object(null)
                .reason(exception.getClass().toString() + "occurred")
                .build();
        return EntityModel.of(errorTemplate)
                .add(
                        WebMvcLinkBuilder.linkTo(MainController.class).withSelfRel()
                );
    }
}
