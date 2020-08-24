package cloud.ptl.itemserver.persistence.conversion.spring;

import cloud.ptl.itemserver.BeanInjector;
import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import cloud.ptl.itemserver.persistence.repositories.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Optional;

@Component
public class AddressEditor extends PropertyEditorSupport {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public String getAsText() {
        AddressDAO addressDAO = (AddressDAO) this.getValue();
        Long id = addressDAO.getId();
        return String.valueOf(id);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Optional<AddressDAO> addressDAO = addressRepository.findById(
                Long.valueOf(text)
        );
        if(addressDAO.isEmpty()) throw new IllegalArgumentException(
                String.format("Date %s unformatable", text)
        );
        this.setValue(addressDAO.get());
    }
}
