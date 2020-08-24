package cloud.ptl.itemserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@EnableJpaRepositories
@EnableWebSecurity
public class ItemServerApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ItemServerApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "DELETE", "PUT");
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
        return reloadableResourceBundleMessageSource;
    }

    @Bean
    public LocaleResolver localeResolver(){
        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver =
                new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return acceptHeaderLocaleResolver;
    }

//    @Bean
//    public LocaleResolver localeResolver(){
//        CookieLocaleResolver cookieLocaleResolver =
//                new CookieLocaleResolver();
//        cookieLocaleResolver.setCookieName("lang");
//        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
//        return cookieLocaleResolver;
//    }

//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor(){
//        LocaleChangeInterceptor localeChangeInterceptor =
//                new LocaleChangeInterceptor();
//        localeChangeInterceptor.setParamName("lang");
//        return localeChangeInterceptor;
//    }

}
