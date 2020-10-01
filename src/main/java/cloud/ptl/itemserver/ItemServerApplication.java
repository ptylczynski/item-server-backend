package cloud.ptl.itemserver;

import cloud.ptl.itemserver.misc.CustomLocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;

@SpringBootApplication
@EnableJpaRepositories
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class ItemServerApplication
    implements WebMvcConfigurer{

    public static void main(String[] args) {
        SpringApplication.run(ItemServerApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH");
    }

    //    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource =
                new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:messages/locale");
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        reloadableResourceBundleMessageSource.setDefaultLocale(Locale.ENGLISH);
        return reloadableResourceBundleMessageSource;
    }

//    @Bean
//    public LocaleResolver localeResolver(){
//        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver =
//                new AcceptHeaderLocaleResolver();
//        acceptHeaderLocaleResolver.setDefaultLocale(Locale.ENGLISH);
//        return acceptHeaderLocaleResolver;
//    }

    @Bean
    public SimpleMailMessage simpleMailMessage(){
        SimpleMailMessage simpleMailMessage =
                new SimpleMailMessage();
        simpleMailMessage.setFrom("no-reply@ptl.cloud");
        simpleMailMessage.setReplyTo("item-server@ptl.cloud");
        return simpleMailMessage;
    }

    @Bean
    public JavaMailSenderImpl javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setPassword("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");
        javaMailSender.setUsername("apikey");
        javaMailSender.setPort(587);
        javaMailSender.setHost("smtp.sendgrid.net");
        javaMailSender.setDefaultEncoding("utf-8");
        javaMailSender.setProtocol("smtp");
        return javaMailSender;
    }
}
