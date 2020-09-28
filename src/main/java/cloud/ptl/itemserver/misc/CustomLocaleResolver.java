package cloud.ptl.itemserver.misc;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.service.implementation.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Service
public class CustomLocaleResolver extends AbstractLocaleResolver {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(CustomLocaleResolver.class);

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        this.logger.info("Resolving locale");
        return this.getLocale(httpServletRequest);
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        this.logger.info("Setting locale");
        locale = this.getLocale(httpServletRequest);
    }

    private Locale getLocaleFromHeader(HttpServletRequest request){
        String header = request.getHeader("Accept-Language");
        return Locale.forLanguageTag(header);
    }

    private Locale getLocale(HttpServletRequest request){
        Locale localeFromDAO = this.userService.getLocale();
        Locale localeFromHeader = this.getLocaleFromHeader(request);
        Locale localeDefault = Locale.US;
        if(localeFromDAO != null) return localeFromDAO;
        else if(localeFromHeader != null){
            this.userService.setLocale(localeFromHeader);
            return localeFromHeader;
        }
        return localeDefault;
    }
}
