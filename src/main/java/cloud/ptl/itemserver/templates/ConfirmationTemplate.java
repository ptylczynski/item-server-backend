package cloud.ptl.itemserver.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

@AllArgsConstructor
@Data
public class ConfirmationTemplate {
    public enum Token{
        DELETE("deleted.successful"),
        ADD("added.successful"),
        UPDATE("update.successful");

        private final String sToken;

        Token(String sToken){
            this.sToken = sToken;
        }

        public String getsToken() {
            return sToken;
        }
    }

    @Autowired
    private MessageSource messageSource;

    private Token token;
    private String className;
    private Link 

    public EntityModel<String> getEntityModel(){
        EntityModel.of(
                this.getMessage(),

        )
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
