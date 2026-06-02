package sumitproject.SpringCart.DTO;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;
import sumitproject.SpringCart.Helper.OrderStatus;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private String userName;
    private AddressDTO address;
    private double totalAmount;
    private double discountAmount;
    private double finalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private PaymentDTO payment;
    private CouponDTO appliedCoupon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
