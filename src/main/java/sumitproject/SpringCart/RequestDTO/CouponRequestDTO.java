package sumitproject.SpringCart.RequestDTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequestDTO {
    @NotBlank(message = "Coupon code is required")
    @Pattern(regexp = "^[A-Z0-9]{4,20}$", message = "Coupon code must be 4-20 alphanumeric uppercase characters")
    private String code;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 0, message = "Discount must be at least 0%")
    @Max(value = 100, message = "Discount cannot exceed 100%")
    private double discountPercent;

    @NotNull(message = "Minimum order amount is required")
    @PositiveOrZero(message = "Minimum order amount must be zero or positive")
    private double minOrderAmount;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @Min(value = 1, message = "Usage limit must be at least 1")
    private Integer usageLimit;
}
