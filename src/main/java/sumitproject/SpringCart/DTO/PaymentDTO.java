package sumitproject.SpringCart.DTO;


import lombok.*;
import sumitproject.SpringCart.Helper.PaymentMethod;
import sumitproject.SpringCart.Helper.PaymentStatus;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime paidAt;
    private String paymentStatusText;
    private Boolean isCompleted;
}