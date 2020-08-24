package cloud.ptl.itemserver.persistence.repositories.item;

import cloud.ptl.itemserver.persistence.dao.item.food.FoodItemDAO;
import cloud.ptl.itemserver.persistence.projections.IdsOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepository extends Repository<FoodItemDAO, Long> {
    Page<FoodItemDAO> findAll(Pageable pageable);
    List<FoodItemDAO> findAll();
    Long count();
    Optional<FoodItemDAO> findById(Long id);
    void save(FoodItemDAO foodItemDAO);
    void deleteById(Long id);
    void delete(FoodItemDAO foodItemDAO);

}
