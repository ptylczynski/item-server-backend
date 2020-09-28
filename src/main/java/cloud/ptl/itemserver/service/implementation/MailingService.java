package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.service.abstract2.AbstractMailingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.Servlet;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class MailingService extends AbstractMailingService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    private final Logger logger = LoggerFactory.getLogger(MailingService.class);

    @Override
    public void sendMail(MailType mailType, UserDAO recipient, Map<String, Object> properties) throws MessagingException, UnsupportedEncodingException {
        this.logger.info("Sending mail");
        this.logger.debug("properties: " + properties.toString());
        MimeMessage message = this.mailSender.createMimeMessage();
        String recipientMail = recipient.getMail();
        this.logger.debug("recipient: " + recipientMail);
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(message);
        mimeMessageHelper.setFrom("noreply@ptl.cloud", "item-server");
        mimeMessageHelper.setReplyTo("item-server@ptl.cloud");
        mimeMessageHelper.setTo(recipientMail);
        mimeMessageHelper.setSubject(
                mailType.getSubject()
        );
        mimeMessageHelper.setText(
                this.getContent(mailType, properties),
                true
        );
        this.logger.debug("message: " + mimeMessageHelper.getMimeMessage().toString());
        this.mailSender.send(message);
    }
    
    private String getContent(MailType mailType, Map<String, Object> properties){
        WebContext webContext = new WebContext(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse(),
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getServletContext(),
                LocaleContextHolder.getLocale(),
                properties
        );
        return templateEngine.process(
                mailType.getTemplateName(),
                webContext
        );
    }
}
