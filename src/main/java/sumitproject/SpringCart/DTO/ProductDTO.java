package sumitproject.SpringCart.DTO;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private String brand;
    private double price;
    private double discountedPrice;
    private Integer stock;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;

}