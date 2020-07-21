package cloud.ptl.itemserver.error.exception.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;
import org.springframework.validation.BindingResult;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ObjectInvalid extends Exception{
    private Object object;
    private BindingResult bindingResult;
    private Link link;
}
