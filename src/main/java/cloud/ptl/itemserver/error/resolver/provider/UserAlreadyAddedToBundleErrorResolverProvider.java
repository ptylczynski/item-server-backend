package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.error.exception.validation.UserAlreadyAddedToBundle;
import cloud.ptl.itemserver.persistence.conversion.dto_assembler.address.FullBundleModelAssembler;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class UserAlreadyAddedToBundleErrorResolverProvider extends AbstractErrorResolverProvider {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

    private final Logger logger = LoggerFactory.getLogger(UserAlreadyAddedToBundle.class);

    @Override
    public boolean canResolve(Class<?> exception) {
        return exception.isAssignableFrom(UserAlreadyAddedToBundle.class);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        this.logger.info("Error resolved as User Already Added To Bundle");
        UserAlreadyAddedToBundle ex = (UserAlreadyAddedToBundle) exception;
        ErrorTemplate errorTemplate = ErrorTemplate.builder()
                .reason(
                        this.messageSource.getMessage(
                                "user.already.added",
                                new Object[]{
                                        ex.getUserDAO().getUsername(),
                                        ex.getBundleDAO().getName()
                                },
                                "Missing reason",
                                LocaleContextHolder.getLocale()
                        )
                )
                .object(
                        this.fullBundleModelAssembler.toModel(ex.getBundleDAO())
                )
                .build();
        return EntityModel.of(
                errorTemplate,
                ex.getLink()
        );
    }
}
