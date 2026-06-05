package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Manage products and inventory")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create product", description = "Creates a new product under a category (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/create/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@PathVariable Long categoryId,
                                                    @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductDTO createdProduct = productService.createProduct(categoryId, productRequestDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all products", description = "Retrieves paginated list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.getAllProducts(pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get products by category", description = "Retrieves all products in a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.getProductsByCategory(categoryId, pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "Search products", description = "Searches products by keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(@RequestParam String keyword,
                                                           @RequestParam(defaultValue = "0") int pageNo,
                                                           @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.searchProducts(keyword, pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "Filter products by price", description = "Retrieves products within a price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/filter-by-price")
    public ResponseEntity<Page<ProductDTO>> getProductsByPriceRange(@RequestParam Double minPrice,
                                                                    @RequestParam Double maxPrice,
                                                                    @RequestParam(defaultValue = "0") int pageNo,
                                                                    @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "Get in-stock products", description = "Retrieves all products that are in stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/in-stock")
    public ResponseEntity<Page<ProductDTO>> getInStockProducts(@RequestParam(defaultValue = "0") int pageNo,
                                                               @RequestParam(defaultValue = "20") int pageSize) {
        Page<ProductDTO> products = productService.getInStockProducts(pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "Delete product", description = "Permanently deletes a product (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update product", description = "Updates an existing product (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable Long id,
                                                        @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductDTO updatedProduct = productService.updateProductById(id, productRequestDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Operation(summary = "Partial update product", description = "Partially updates an existing product (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> partialUpdateProductById(@PathVariable Long id,
                                                               @RequestBody ProductRequestDTO productRequestDTO) {
        ProductDTO updatedProduct = productService.partialUpdateProductById(id, productRequestDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Operation(summary = "Update stock", description = "Updates the stock quantity of a product (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        ProductDTO updatedProduct = productService.updateStock(id, quantity);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}