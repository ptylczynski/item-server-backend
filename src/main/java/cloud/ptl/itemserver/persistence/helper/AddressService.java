package cloud.ptl.itemserver.persistence.helper;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.repositories.address.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    private final Logger logger = LoggerFactory.getLogger(AddressService.class);

    public Boolean checkIfAddressExists(Long id) throws ObjectNotFound {
        this.logger.info("Checking if addreess id=" + id + " exists");
        if(!this.addressRepository.existsById(id)){
            this.logger.debug("Address does not exist");
            throw new ObjectNotFound(
                    id,
                    null
            );
        }
        this.logger.debug("Address exists");
        return true;
    }
}
