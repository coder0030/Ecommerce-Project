package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.ProductDTO;
import sumitproject.SpringCart.RequestDTO.ProductRequestDTO;
import sumitproject.SpringCart.Service.ProductService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Override
    public ProductDTO createProduct(Long categoryId, ProductRequestDTO productRequestDTO) {
        return null;
    }

    @Override
    public Page<ProductDTO> getAllProducts(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return null;
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long categoryId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<ProductDTO> searchProducts(String keyword, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<ProductDTO> getInStockProducts(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public void deleteProductById(Long id) {

    }

    @Override
    public ProductDTO updateProductById(Long id, ProductRequestDTO productRequestDTO) {
        return null;
    }

    @Override
    public ProductDTO partialUpdateProductById(Long id, ProductRequestDTO productRequestDTO) {
        return null;
    }

    @Override
    public ProductDTO updateStock(Long id, Integer quantity) {
        return null;
    }
}
