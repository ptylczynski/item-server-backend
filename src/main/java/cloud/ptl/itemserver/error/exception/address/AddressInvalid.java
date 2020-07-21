package cloud.ptl.itemserver.error.exception.address;

import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddressInvalid extends Exception {
    private AddressDAO addressDAO;

    public AddressInvalid(AddressDAO addressDAO){
        this.addressDAO = addressDAO;
    }
}
