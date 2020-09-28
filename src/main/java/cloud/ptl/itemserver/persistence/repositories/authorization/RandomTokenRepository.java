package cloud.ptl.itemserver.persistence.repositories.authorization;

import cloud.ptl.itemserver.persistence.dao.authentication.RandomTokenDAO;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RandomTokenRepository extends JpaRepository<RandomTokenDAO, Long> {
    List<RandomTokenDAO> findRandomTokenDAOSByOwner(UserDAO owner, Pageable pageable);
    Optional<RandomTokenDAO> findRandomTokenDAOByToken(String token);
    void deleteRandomTokenDAOByToken(String token);
}
