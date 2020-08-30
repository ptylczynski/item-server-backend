package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.error.exception.validation.UserNotAddedToBundle;
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
public class UserNotAddedToBundleResolverProvider extends AbstractErrorResolverProvider {

    @Autowired
    private FullBundleModelAssembler fullBundleModelAssembler;

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(UserNotAddedToBundle.class);

    @Override
    public boolean canResolve(Class<?> exception) {
        return exception.isAssignableFrom(UserNotAddedToBundle.class);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        this.logger.info("Error resolved as User Not Added To Bundle");
        UserNotAddedToBundle ex = (UserNotAddedToBundle) exception;
        ErrorTemplate errorTemplate = ErrorTemplate.builder()
                .object(
                        this.fullBundleModelAssembler.toModel(ex.getBundleDAO())
                )
                .reason(
                        this.messageSource.getMessage(
                                "user.not.added",
                                new Object[]{},
                                LocaleContextHolder.getLocale()
                        )
                )
                .build();
        return EntityModel.of(
                errorTemplate,
                ex.getLink()
        );
    }
}
