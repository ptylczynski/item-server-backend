package cloud.ptl.itemserver.persistence.repositories.bundle;

import cloud.ptl.itemserver.persistence.dao.bundle.BundleDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface BundleRepository extends JpaRepository<BundleDAO, Long> {
    Page<BundleDAO> findAll(Pageable pageable);
    <T> ArrayList<T> findBy(Class<T> type);
    <T> Optional<T> findById(Long id, Class<T> type);
}
