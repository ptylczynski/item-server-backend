package cloud.ptl.itemserver.error.exception.validation;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserAlreadyAddedToBundle extends Exception {
    private BundleDAO bundleDAO;
    private UserDAO userDAO;
    private Link link;
}
