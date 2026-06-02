package sumitproject.SpringCart.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    
    Page<Category> findAllByParentCategory_IdAndIsActiveTrue(Long parentId, Pageable pageable);

    Page<Category> findAllByIsActiveTrue(Pageable pageable);

    List<Category> findByParentCategoryIsNullAndIsActiveTrue();

    List<Category> findByParentCategory_IdAndIsActiveTrue(Long parentId);

    boolean existsByParentCategory_IdAndIsActiveTrue(Long id);
}
