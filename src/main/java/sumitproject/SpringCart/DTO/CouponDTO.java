package sumitproject.SpringCart.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private Long id;
    private String code;
    private double discountPercent;
    private double minOrderAmount;
    private LocalDate expiryDate;
    private Integer usageLimit;
    private Integer usedCount;
    private Boolean isValid;
    private Boolean isActive;
    private LocalDateTime createdAt;

}
