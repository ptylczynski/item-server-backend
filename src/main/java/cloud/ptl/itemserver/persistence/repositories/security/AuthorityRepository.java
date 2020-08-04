package cloud.ptl.itemserver.persistence.repositories.security;

import cloud.ptl.itemserver.persistence.dao.authentication.AuthorityDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityDAO, Long> {
}
