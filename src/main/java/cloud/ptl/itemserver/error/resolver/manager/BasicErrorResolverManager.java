package cloud.ptl.itemserver.error.resolver.manager;

import cloud.ptl.itemserver.templates.ErrorTemplate;
import cloud.ptl.itemserver.error.resolver.provider.AbstractErrorResolverProvider;
import cloud.ptl.itemserver.error.resolver.provider.ObjectInvalidResolverProvider;
import cloud.ptl.itemserver.error.resolver.provider.ObjectNotFoundResolverProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class BasicErrorResolverManager extends ErrorResolverManager {

    @Autowired
    private ObjectNotFoundResolverProvider objectNotFoundResolverProvider;

    @Autowired
    private ObjectInvalidResolverProvider objectInvalidResolverProvider;

    private ArrayList<AbstractErrorResolverProvider> providers;

    @PostConstruct
    public void init(){
        this.providers = new ArrayList<>();
        this.providers.add(objectInvalidResolverProvider);
        this.providers.add(objectNotFoundResolverProvider);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        for(AbstractErrorResolverProvider provider : providers){
            if(provider.canResolve(exception.getClass())){
                return provider.resolve(exception);
            }
        }
        return EntityModel.of(
                ErrorTemplate.builder()
                        .reason(
                                String.format("No proper resolver for %s found",
                                        exception.getClass().toString())
                        )
                        .object(exception).build(),
                Link.of("")
        );
    }
}
