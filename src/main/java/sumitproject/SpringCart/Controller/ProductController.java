package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.ProductDTO;
import sumitproject.SpringCart.RequestDTO.ProductRequestDTO;
import sumitproject.SpringCart.Service.ProductService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@PathVariable Long categoryId,
                                                    @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductDTO createdProduct = productService.createProduct(categoryId, productRequestDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.getAllProducts(pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.getProductsByCategory(categoryId, pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(@RequestParam String keyword,
                                                           @RequestParam(defaultValue = "0") int pageNo,
                                                           @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.searchProducts(keyword, pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/filter-by-price")
    public ResponseEntity<Page<ProductDTO>> getProductsByPriceRange(@RequestParam Double minPrice,
                                                                    @RequestParam Double maxPrice,
                                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                                    @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<Page<ProductDTO>> getInStockProducts(@RequestParam(defaultValue = "0") int pageNo,
                                                               @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.getInStockProducts(pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable Long id,
                                                        @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductDTO updatedProduct = productService.updateProductById(id, productRequestDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> partialUpdateProductById(@PathVariable Long id,
                                                               @RequestBody ProductRequestDTO productRequestDTO) {
        ProductDTO updatedProduct = productService.partialUpdateProductById(id, productRequestDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        ProductDTO updatedProduct = productService.updateStock(id, quantity);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}