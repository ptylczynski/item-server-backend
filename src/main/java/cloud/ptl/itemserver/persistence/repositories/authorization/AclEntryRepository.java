package cloud.ptl.itemserver.persistence.repositories.authorization;

import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclEntryDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclIdentityDAO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AclEntryRepository extends JpaRepository<AclEntryDAO, Long> {
    Optional<AclEntryDAO> findAclEntryDAOByAclIdentityDAOAndUserDAO(AclIdentityDAO aclIdentityDAO, UserDAO userDAO);
    List<AclEntryDAO> findAclEntryDAOByUserDAOAndAclIdentityDAO_Clazz(UserDAO userDAO, String clazz, Pageable pageable);
}
