package cloud.ptl.itemserver.persistence.repositories.security;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDAO, Long> {
    Optional<UserDAO> findByUsername(String username);
}
