package cloud.ptl.itemserver.error.exception.parsing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;

@Data
@EqualsAndHashCode(callSuper = true)
public class ObjectUnformatable extends IllegalArgumentException {
    private Link link;
}
