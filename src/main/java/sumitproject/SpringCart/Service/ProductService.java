package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.ProductDTO;
import sumitproject.SpringCart.RequestDTO.ProductRequestDTO;

import java.math.BigDecimal;

@Component
public interface ProductService {
    ProductDTO createProduct(Long categoryId, @Valid ProductRequestDTO productRequestDTO);

    Page<ProductDTO> getAllProducts(int pageNo, int pageSize);

    ProductDTO getProductById(Long id);

    Page<ProductDTO> getProductsByCategory(Long categoryId, int pageNo, int pageSize);

    Page<ProductDTO> searchProducts(String keyword, int pageNo, int pageSize);

    Page<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int pageNo, int pageSize);

    Page<ProductDTO> getInStockProducts(int pageNo, int pageSize);

    void deleteProductById(Long id);

    ProductDTO updateProductById(Long id, @Valid ProductRequestDTO productRequestDTO);

    ProductDTO partialUpdateProductById(Long id, ProductRequestDTO productRequestDTO);

    ProductDTO updateStock(Long id, Integer quantity);
}
