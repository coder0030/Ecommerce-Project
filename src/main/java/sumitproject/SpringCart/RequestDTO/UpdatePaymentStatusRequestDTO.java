package sumitproject.SpringCart.RequestDTO;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sumitproject.SpringCart.Helper.PaymentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentStatusRequestDTO {

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    private String transactionId;

    private String failureReason;
}
