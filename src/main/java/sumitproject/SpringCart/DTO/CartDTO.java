package sumitproject.SpringCart.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private Long userId;
    private double totalPrice;
    private List<CartItemDTO> items;
    private Integer totalItems;
    private LocalDateTime updatedAt;

}
