package cloud.ptl.itemserver.templates;

import cloud.ptl.itemserver.BeanInjector;
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

@Data
public class ConfirmationTemplate {

    public enum Token{
        DELETE("deleted.successful"),
        ADD("added.successful"),
        PUT("put.successful");

        private final String sToken;

        Token(String sToken){
            this.sToken = sToken;
        }

        public String getsToken() {
            return sToken;
        }
    }

    private MessageSource messageSource;

    private Token token;
    private String className;
    private Link link;

    public ConfirmationTemplate(Token token, String className, Link link){
        this.token = token;
        this.className = className;
        this.link = link;

        this.messageSource = (MessageSource) BeanInjector.getBean(MessageSource.class);
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
                new String[]{this.className},
                "No confirmation message found",
                LocaleContextHolder.getLocale()
        );
    }
}
