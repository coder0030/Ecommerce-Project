package sumitproject.SpringCart.RequestDTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateRequestDTO {

    @NotNull(message = "Address ID is required")
    private Long addressId;

    @NotNull(message = "User ID is required")
    private Long orderId;

    @NotNull(message = "Address ID is required")
    private Long newAddressId;


}