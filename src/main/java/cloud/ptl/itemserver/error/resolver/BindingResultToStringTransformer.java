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
        StringBuilder sb = new StringBuilder();
        for(ObjectError error : bindingResult.getAllErrors()){
            sb.append(

                    messageSource.getMessage(
                            error.getCodes()[0],
                            new Object[0],
                            Arrays.toString(error.getCodes()),
                            LocaleContextHolder.getLocale())
            );
        }
        return sb.toString();
    }
}
