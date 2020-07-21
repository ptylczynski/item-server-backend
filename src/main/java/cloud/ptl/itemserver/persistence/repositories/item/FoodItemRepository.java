package cloud.ptl.itemserver.persistence.repositories.item;

import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface FoodItemRepository extends JpaRepository<FoodItemDAO, Long> {
    Page<FoodItemDAO> findAll(Pageable pageable);
}
