package cloud.ptl.itemserver.error.resolver.provider;

import cloud.ptl.itemserver.controllers.MainController;
import cloud.ptl.itemserver.templates.ErrorTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.sql.SQLIntegrityConstraintViolationException;

@Component
public class DataIntegrityViolationResolverProvider extends AbstractErrorResolverProvider{

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean canResolve(Class<?> exception) {
        return org.springframework.dao.DataIntegrityViolationException.class.isAssignableFrom(exception);
    }

    @Override
    public <E extends Exception> EntityModel<ErrorTemplate> resolve(E exception) {
        ErrorTemplate errorTemplate = ErrorTemplate.builder()
                .reason(this.getMessage())
                .object(null).build();
        return EntityModel.of(
                errorTemplate,
                WebMvcLinkBuilder.linkTo(MainController.class).withSelfRel()
        );
    }

    private String getMessage(){
        return this.messageSource.getMessage(
                "sql.integrity.violation",
                new Object[]{},
                "Missing SQL constraints violation description",
                LocaleContextHolder.getLocale()
        );
    }
}
