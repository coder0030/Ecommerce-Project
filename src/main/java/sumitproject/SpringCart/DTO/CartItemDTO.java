package sumitproject.SpringCart.DTO;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long productId;
    private Long cartId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private double price;
    private double subtotal;
    private LocalDateTime addedAt;

}
