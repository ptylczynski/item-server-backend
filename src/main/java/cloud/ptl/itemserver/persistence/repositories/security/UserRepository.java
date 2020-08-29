package cloud.ptl.itemserver.persistence.repositories.security;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDAO, Long> {
    Optional<UserDAO> findByUsername(String username);
    <T> Optional<T> findByMail(String mail);
    <T> Optional<T> findById(Long id, Class<T> type);
    <T> List<T> findBy(Class<T> type);
    Boolean existsByUsername(String username);
    Boolean existsByMail(String mail);
}
