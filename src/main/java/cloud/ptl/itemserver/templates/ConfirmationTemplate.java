package cloud.ptl.itemserver.templates;

import cloud.ptl.itemserver.BeanInjector;
import cloud.ptl.itemserver.error.resolver.transformers.ClassNameToStringTransformer;
import lombok.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

public class ConfirmationTemplate {

    private final ClassNameToStringTransformer classNameToStringTransformer;

    public enum Token{
        DELETE("deleted.successful"),
        ADD("added.successful"),
        PUT("put.successful"),
        PATCH("patch.successful");

        private final String sToken;

        Token(String sToken){
            this.sToken = sToken;
        }

        public String getsToken() {
            return sToken;
        }
    }

    private final MessageSource messageSource;

    private final Token token;
    private final String className;
    private final Link link;

    public ConfirmationTemplate(Token token, String className, Link link){
        this.token = token;
        this.className = className;
        this.link = link;

        this.messageSource =
                (MessageSource) BeanInjector.getBean(MessageSource.class);
        this.classNameToStringTransformer =
                (ClassNameToStringTransformer) BeanInjector.getBean(ClassNameToStringTransformer.class);
    }

    public EntityModel<String> getEntityModel(){
        return EntityModel.of(
                this.getMessage(),
                link
        );
    }

    private String getMessage(){
        return this.messageSource.getMessage(
                this.token.getsToken(),
                new String[]{
                     this.classNameToStringTransformer.transform(this.className)
                },
                "No confirmation message found",
                LocaleContextHolder.getLocale()
        );
    }
}
