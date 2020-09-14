package cloud.ptl.itemserver.persistence.repositories.authorization;

import cloud.ptl.itemserver.persistence.dao.authorization.AclIdentityDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AclIdentityRepository extends JpaRepository<AclIdentityDAO, Long> {
    Optional<AclIdentityDAO> findAclIdentityDAOByClazzAndObjectId(String clazz, Long objectId);
}
