package cloud.ptl.itemserver.error.exception.validation;

import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserNotAddedToBundle extends Exception{
    private Link link;
    private BundleDAO bundleDAO;
}
