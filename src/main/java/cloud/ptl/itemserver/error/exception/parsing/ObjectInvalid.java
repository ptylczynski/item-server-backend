package cloud.ptl.itemserver.error.exception.parsing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@ResponseStatus(
        code = HttpStatus.BAD_REQUEST,
        reason = "object has invalid structure"
)
public class ObjectInvalid extends Exception{
    private Object object;
    private BindingResult bindingResult;
    private Link link;
}
