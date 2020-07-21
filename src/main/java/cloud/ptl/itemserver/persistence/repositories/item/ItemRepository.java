package cloud.ptl.itemserver.persistence.repositories.item;

import cloud.ptl.itemserver.persistence.dao.item.generics.ItemDAO;
import org.springframework.data.repository.CrudRepository;

interface ItemRepository extends CrudRepository<ItemDAO, Long> {
}
