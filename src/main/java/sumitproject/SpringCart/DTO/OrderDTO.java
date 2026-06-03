package sumitproject.SpringCart.DTO;

import lombok.*;
import sumitproject.SpringCart.Helper.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String couponCode;
    private Double totalAmount;
    private Double discountAmount;
    private Double finalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String deliveryName;
    private String deliveryPhone;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryPincode;
    private String deliveryCountry;

    private List<OrderItemDTO> orderItems;
    private PaymentDTO payment;
}