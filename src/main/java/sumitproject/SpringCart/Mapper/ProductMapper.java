package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.ProductDTO;
import sumitproject.SpringCart.Entity.Category;
import sumitproject.SpringCart.Entity.Product;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.RequestDTO.ProductRequestDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO toDto(Product product, Category category) {
        if (product == null) return null;

        ProductDTO.ProductDTOBuilder builder = ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .price(product.getPrice())
                .discountedPrice(product.getDiscount())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .categoryId(product.getCategoryId());

        if (category != null) {
            builder.categoryName(category.getName());
        }

        return builder.build();
    }

    public Product toEntity(ProductRequestDTO productDTO, Product product) {
        if (productDTO == null) return null;

        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getBrand() != null) {
            product.setBrand(productDTO.getBrand());
        }

        if (productDTO.getStock() != null) {
            product.setStock(productDTO.getStock());
        }
        if (productDTO.getImageUrl() != null) {
            product.setImageUrl(productDTO.getImageUrl());
        }

        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());

        return product;
    }

    public List<ProductDTO> toDtoList(List<Product> products, Category category) {
        if (products == null || products.isEmpty()) return null;
        return products.stream()
                .map(product -> toDto(product, category))
                .collect(Collectors.toList());
    }

    public Product updateToEntity(ProductRequestDTO productDTO, Product product) {
        boolean nullValue = false;

        if (productDTO.getName() == null) nullValue = true;
        if (productDTO.getStock() == null) nullValue = true;

        if (nullValue) {
            throw new BadRequestException("Incomplete data provided. Name, price, and stock are required.");
        }

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());

        return product;
    }
}
