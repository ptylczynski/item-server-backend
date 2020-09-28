package cloud.ptl.itemserver.error.exception.parsing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ResponseStatus(
        code = HttpStatus.BAD_REQUEST,
        reason = "object is unformatable"
)
public class ObjectUnformatable extends IllegalArgumentException {
    private Link link;
}
