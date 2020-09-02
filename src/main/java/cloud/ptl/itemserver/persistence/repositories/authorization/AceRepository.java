package cloud.ptl.itemserver.persistence.repositories.authorization;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AceDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AceRepository extends JpaRepository<AceDAO, Long> {
    Optional<AceDAO> findBySecurityHashAndUserDAO(String securityHash, UserDAO userDAO);
}
