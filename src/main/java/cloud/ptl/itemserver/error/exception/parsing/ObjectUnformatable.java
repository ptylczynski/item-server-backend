package cloud.ptl.itemserver.error.exception.parsing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ObjectUnformatable extends IllegalArgumentException {
    private Link link;
}
