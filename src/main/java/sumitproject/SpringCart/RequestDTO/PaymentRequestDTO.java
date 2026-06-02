package sumitproject.SpringCart.RequestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import sumitproject.SpringCart.Helper.PaymentMethod;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Payment amount is required")
    @Positive(message = "Amount must be positive")
    private double amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String transactionId;
    private String upiId;

    private String cardNumber;
    private String cardExpiry;
    private String cardCvv;
}