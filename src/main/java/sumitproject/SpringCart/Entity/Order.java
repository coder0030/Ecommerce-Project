package sumitproject.SpringCart.Entity;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "discount_percent", nullable = false)
    private double discountPercent;

    @Column(name = "min_order_amount")
    private double minOrderAmount;

    @Column(name = "expiry_date", nullable = false)
    private double expiryDate;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void setUp() {
        createdAt = LocalDateTime.now();
        isActive = true;
    }
}