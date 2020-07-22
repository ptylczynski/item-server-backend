package cloud.ptl.itemserver.error.resolver;

import org.aspectj.bridge.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;

@Component
public class BindingResultToStringTransformer {

    @Autowired
    private MessageSource messageSource;

    public String transform(BindingResult bindingResult){
        for(ObjectError error : bindingResult.getAllErrors()){
            if(error.getCodes() != null) {
                for (String code : error.getCodes()){
                    String message = messageSource.getMessage(
                            code,
                            new Object[0],
                            "error",
                            LocaleContextHolder.getLocale()
                    );
                    if(!message.equals("error")) return message;
                }
            }
        }
        return Arrays.toString(bindingResult.getAllErrors().toArray());
    }
}
