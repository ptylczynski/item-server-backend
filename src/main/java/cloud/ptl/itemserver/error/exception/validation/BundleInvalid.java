package cloud.ptl.itemserver.error.exception.validation;

import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BundleInvalid extends Exception {
    private BundleDAO addressDAO;

    public BundleInvalid(BundleDAO bundleDAO){
        this.addressDAO = bundleDAO;
    }
}
