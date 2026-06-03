package sumitproject.SpringCart.Repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.Product;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByNameAndIsActiveTrue(String name);
    
    Product findByIdAndIsActiveTrue(Long id);

    Page<Product> findByCategory_IdAndIsActiveTrue(Long categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String keyword, Pageable pageable);

    Page<Product> findByPriceBetweenAndIsActiveTrue(Double minPrice, Double maxPrice, Pageable pageable);

    Page<Product> findByStockGreaterThanAndIsActiveTrue(int i, Pageable pageable);

    Page<Product> findAllByIsActiveTrue(Pageable pageable);

    boolean existsByNameAndIdNotAndIsActiveTrue(@NotBlank(message = "Product name is required") String name, Long id);
}
