package cloud.ptl.itemserver.persistence.repositories.address;

import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends JpaRepository<AddressDAO, Long> {
    Page<AddressDAO> findAll(Pageable pageable);
}
