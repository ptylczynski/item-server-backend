package cloud.ptl.itemserver.error.resolver.transformers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;

@Component
public class BindingResultToStringTransformer extends AbstractTransformer<BindingResult, String> {

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(BindingResultToStringTransformer.class);



    public String transform(BindingResult bindingResult){
        this.logger.info("Transforming binding result into string");
        for(ObjectError error : bindingResult.getAllErrors()){
            if(error.getCodes() != null) {
                for (String code : error.getCodes()){
                    String message = messageSource.getMessage(
                            code,
                            new Object[0],
                            "error",
                            LocaleContextHolder.getLocale()
                    );
                    if(!message.equals("error")){
                        this.logger.debug("Resolved as: " + message);
                        return message;
                    }
                }
            }
        }
        this.logger.debug("Binding result unresolvable");
        return Arrays.toString(bindingResult.getAllErrors().toArray());
    }
}

