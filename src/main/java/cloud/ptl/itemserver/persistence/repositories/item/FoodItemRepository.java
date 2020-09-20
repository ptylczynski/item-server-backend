package cloud.ptl.itemserver.persistence.repositories.item;

import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepository extends JpaRepository<FoodItemDAO, Long> {
    Page<FoodItemDAO> findAllByIdIn(List<Long> ids, Pageable pageable);
    List<FoodItemDAO> findAll();
    Optional<FoodItemDAO> findById(Long id);
}
