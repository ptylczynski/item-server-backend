package cloud.ptl.itemserver.error.exception.missing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@ResponseStatus(
        code = HttpStatus.NOT_FOUND,
        reason = "object was not found"
)
public class ObjectNotFound extends Exception{
    private String objectClass;
    private Link link;
}
