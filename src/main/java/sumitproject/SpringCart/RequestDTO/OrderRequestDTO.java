package sumitproject.SpringCart.RequestDTO;

import jakarta.validation.constraints.*;
import lombok.*;
import sumitproject.SpringCart.Helper.PaymentMethod;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotNull(message = "Address ID is required")
    private Long addressId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String couponCode;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
