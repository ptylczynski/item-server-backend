package cloud.ptl.itemserver.service.abstract2;

import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class AbstractDAOService<T> {
    public abstract List<T> findAll(Pageable pageable, AclPermission permission);
    public abstract T findById(Long id) throws ObjectNotFound;
    public abstract Boolean hasAccess(T item, AclPermission permission);
    public abstract Boolean checkIfExists(Long id) throws ObjectNotFound;
}
