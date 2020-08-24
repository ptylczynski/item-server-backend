package cloud.ptl.itemserver.error.exception.missing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ObjectNotFound extends Exception{
    private Object discriminator;
    private Link link;
}
