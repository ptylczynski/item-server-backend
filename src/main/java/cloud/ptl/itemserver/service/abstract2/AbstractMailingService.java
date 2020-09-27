package cloud.ptl.itemserver.service.abstract2;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class AbstractMailingService {

    @Autowired
    TemplateEngine templateEngine;

    public enum MailType{
        ACTIVATE_ACC("register_mail", "Account activation");


        @Getter
        private final String templateName;

        @Getter
        private final String subject;

        MailType(String templateName, String subject) {
            this.templateName = templateName;
            this.subject = subject;
        }
    }
    public abstract void sendMail(MailType mailType, UserDAO recipient, Map<String, Object> properties) throws MessagingException, UnsupportedEncodingException;
}
