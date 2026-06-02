package sumitproject.SpringCart.DTO;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private Long orderId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private double priceAtPurchase;
    private double subtotal;

}
