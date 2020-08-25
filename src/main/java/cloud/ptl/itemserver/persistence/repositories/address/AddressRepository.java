package cloud.ptl.itemserver.persistence.repositories.address;

import cloud.ptl.itemserver.persistence.dao.address.AddressDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressDAO, Long> {
    Page<AddressDAO> findAll(Pageable pageable);
    <T> ArrayList<T> findBy(Class<T> type);
    <T> Optional<T> findById(Long id, Class<T> type);
}
