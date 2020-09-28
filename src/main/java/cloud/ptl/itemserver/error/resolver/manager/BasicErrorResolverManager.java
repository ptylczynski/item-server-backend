package cloud.ptl.itemserver.error.resolver.manager;

import cloud.ptl.itemserver.error.resolver.provider.*;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private DataIntegrityViolationResolverProvider dataIntegrityViolationResolverProvider;

    @Autowired
    private InsufficientPermissionResolverProvider insufficientPermissionResolverProvider;

    private ArrayList<AbstractErrorResolverProvider> providers;
    private final Logger logger = LoggerFactory.getLogger(BasicErrorResolverManager.class);

    @PostConstruct
    public void init(){
        this.providers = new ArrayList<>();
        this.providers.add(objectInvalidResolverProvider);
        this.providers.add(objectNotFoundResolverProvider);
        this.providers.add(dataIntegrityViolationResolverProvider);
        this.providers.add(insufficientPermissionResolverProvider);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        this.logger.info("Matching resolver to error");
        this.logger.debug("error: " + exception.toString());
        for(AbstractErrorResolverProvider provider : providers){
            if(provider.canResolve(exception.getClass())){
                return provider.resolve(exception);
            }
        }
        this.logger.debug("No resolver found");
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
