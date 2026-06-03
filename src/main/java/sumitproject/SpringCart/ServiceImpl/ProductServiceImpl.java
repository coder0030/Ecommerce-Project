package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumitproject.SpringCart.DTO.ProductDTO;
import sumitproject.SpringCart.Entity.Category;
import sumitproject.SpringCart.Entity.Product;
import sumitproject.SpringCart.Mapper.ProductMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.Repository.ProductRepository;
import sumitproject.SpringCart.RequestDTO.ProductRequestDTO;
import sumitproject.SpringCart.Service.ProductService;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AllRepositoryMethods allRepositoryMethods;

    private void checkExistence(String name) {
        if (productRepository.existsByNameAndIsActiveTrue(name)) {
            throw new BadRequestException("Product name: " + name + " already exists.");
        }
    }

    private void validatePrice(Double price) {
        if (price == null || price <= 0) {
            throw new BadRequestException("Product price must be greater than zero");
        }
    }

    private void validateStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new BadRequestException("Product stock cannot be negative");
        }
    }

    private void validateDiscount(Double discount) {
        if (discount != null && (discount < 0 || discount > 100)) {
            throw new BadRequestException("Discount must be between 0 and 100");
        }
    }

    @Override
    public ProductDTO createProduct(Long categoryId, ProductRequestDTO productRequestDTO) {
        validatePrice(productRequestDTO.getPrice());
        validateStock(productRequestDTO.getStock());
        validateDiscount(productRequestDTO.getDiscount());

        Category category = allRepositoryMethods.getCategoryById(categoryId);

        checkExistence(productRequestDTO.getName());

        Product product = new Product();
        product = productMapper.toEntity(productRequestDTO, product);
        product.setIsActive(true);

        category.addProduct(product);

        Product saved = productRepository.save(product);

        return productMapper.toDto(saved);
    }

    @Override
    public Page<ProductDTO> getAllProducts(int pageNo, int pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> productPage = productRepository.findAllByIsActiveTrue(pageable);
        if (productPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return productPage.map(productMapper::toDto);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = allRepositoryMethods.getProductById(id);
        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long categoryId, int pageNo, int pageSize) {
        Category category = allRepositoryMethods.getCategoryById(categoryId);

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> productPage = productRepository.findByCategory_IdAndIsActiveTrue(categoryId, pageable);
        if (productPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDTO> searchProducts(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Product> productPage = productRepository
                .findByNameContainingIgnoreCaseAndIsActiveTrue(keyword, pageable);

        if (productPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDTO> getProductsByPriceRange(Double minPrice, Double maxPrice, int pageNo, int pageSize) {
        if (minPrice == null || maxPrice == null) {
            throw new BadRequestException("Min price and max price are required");
        }
        if (minPrice > maxPrice) {
            throw new BadRequestException("Min price cannot be greater than max price");
        }

        Sort sort = Sort.by("price").ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> productPage = productRepository
                .findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice, pageable);

        if (productPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDTO> getInStockProducts(int pageNo, int pageSize) {
        Sort sort = Sort.by("price").ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> productPage = productRepository
                .findByStockGreaterThanAndIsActiveTrue(0, pageable);

        if (productPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return productPage.map(productMapper::toDto);
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = allRepositoryMethods.getProductById(id);
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Override
    public ProductDTO updateProductById(Long id, ProductRequestDTO productRequestDTO) {
        Product product = allRepositoryMethods.getProductById(id);


        if (productRequestDTO.getName() != null && !productRequestDTO.getName().equals(product.getName())) {
            if (productRepository.existsByNameAndIdNotAndIsActiveTrue(productRequestDTO.getName(), id)) {
                throw new BadRequestException("Product name: " + productRequestDTO.getName() + " already exists.");
            }
        }

        if (productRequestDTO.getStock() != null) {
            validateStock(productRequestDTO.getStock());
        }

        product = productMapper.updateToEntity(productRequestDTO, product);

        if (productRequestDTO.getCategoryId() != null &&
                (product.getCategory() == null ||
                        !productRequestDTO.getCategoryId().equals(product.getCategory().getId()))) {

            Category oldCategory = product.getCategory();
            Category newCategory =
                    allRepositoryMethods.getCategoryById(productRequestDTO.getCategoryId());

            oldCategory.removeProduct(product);

            newCategory.addProduct(product);
            product.setCategory(newCategory);
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public ProductDTO partialUpdateProductById(Long id, ProductRequestDTO productRequestDTO) {
        Product product = allRepositoryMethods.getProductById(id);

        if (productRequestDTO.getName() != null && !productRequestDTO.getName().equals(product.getName())) {
            if (productRepository.existsByNameAndIdNotAndIsActiveTrue(productRequestDTO.getName(), id)) {
                throw new BadRequestException("Product name: " + productRequestDTO.getName() + " already exists.");
            }
            product.setName(productRequestDTO.getName());
        }

        if (productRequestDTO.getDescription() != null) {
            product.setDescription(productRequestDTO.getDescription());
        }

        if (productRequestDTO.getBrand() != null) {
            product.setBrand(productRequestDTO.getBrand());
        }

        if (productRequestDTO.getPrice() != null && productRequestDTO.getPrice() > 0) {
            validatePrice(productRequestDTO.getPrice());
            product.setPrice(productRequestDTO.getPrice());
        }

        if (productRequestDTO.getDiscount() != null) {
            validateDiscount(productRequestDTO.getDiscount());
            product.setDiscount(productRequestDTO.getDiscount());
        }

        if (productRequestDTO.getStock() != null && productRequestDTO.getStock() >= 0) {
            validateStock(productRequestDTO.getStock());
            product.setStock(productRequestDTO.getStock());
        }

        if (productRequestDTO.getImageUrl() != null) {
            product.setImageUrl(productRequestDTO.getImageUrl());
        }

        if (productRequestDTO.getCategoryId() != null &&
                (product.getCategory() == null ||
                        !productRequestDTO.getCategoryId().equals(product.getCategory().getId()))) {

            Category oldCategory = product.getCategory();
            Category newCategory =
                    allRepositoryMethods.getCategoryById(productRequestDTO.getCategoryId());

            oldCategory.removeProduct(product);

            newCategory.addProduct(product);
            product.setCategory(newCategory);
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public ProductDTO updateStock(Long id, Integer quantity) {
        if (quantity == null) {
            throw new BadRequestException("Quantity is required");
        }

        Product product = allRepositoryMethods.getProductById(id);

        int currentStock = product.getStock();
        int newStock = currentStock + quantity;

        if (newStock < 0) {
            throw new BadRequestException("Insufficient stock. Current stock: " + currentStock +
                    ", Requested reduction: " + Math.abs(quantity));
        }

        product.setStock(newStock);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }

    public ProductDTO setStock(Long id, Integer newStock) {
        if (newStock == null || newStock < 0) {
            throw new BadRequestException("Stock must be zero or positive");
        }

        Product product = allRepositoryMethods.getProductById(id);
        product.setStock(newStock);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }
}