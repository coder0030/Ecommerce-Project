package sumitproject.SpringCart.RequestDTO;

import jakarta.validation.constraints.*;
import lombok.*;
import sumitproject.SpringCart.Helper.OrderStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequestDTO {
    @NotNull(message = "Order status is required")
    private OrderStatus status;

    private String cancellationReason;
}
