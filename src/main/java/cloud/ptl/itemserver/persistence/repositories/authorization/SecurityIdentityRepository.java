package cloud.ptl.itemserver.persistence.repositories.authorization;

import cloud.ptl.itemserver.persistence.dao.authorization.SecurityIdentityDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityIdentityRepository extends JpaRepository<SecurityIdentityDAO, Long> {
}
