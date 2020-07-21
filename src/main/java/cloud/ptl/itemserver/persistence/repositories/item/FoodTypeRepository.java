package cloud.ptl.itemserver.persistence.repositories.item;

import cloud.ptl.itemserver.persistence.dao.item.food.FoodTypeDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface FoodTypeRepository extends JpaRepository<FoodTypeDAO, Long> {
    Page<FoodTypeDAO> findAll(Pageable pageable);
}
