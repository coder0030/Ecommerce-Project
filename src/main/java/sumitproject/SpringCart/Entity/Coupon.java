package sumitproject.SpringCart.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "coupons", indexes = @Index(name = "coupon_id", columnList = "id")
)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "discount_percent", nullable = false)
    private Double discountPercent;

    @Column(name = "min_order_amount")
    private Double minOrderAmount;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (minOrderAmount == null) minOrderAmount = 0.0;
        if (usageLimit == null) usageLimit = 1;
        if (usedCount == null) usedCount = 0;
        if (isActive == null) isActive = true;
    }

    public boolean isValid() {
        return isActive &&
                LocalDate.now().isBefore(expiryDate) &&
                usedCount < usageLimit;
    }

    public boolean canApply(Double orderAmount) {
        return isValid() && orderAmount >= minOrderAmount;
    }

    public Double applyDiscount(Double orderAmount) {
        if (!canApply(orderAmount)) {
            return orderAmount;
        }
        Double discount = orderAmount * (discountPercent / 100);
        return orderAmount - discount;
    }

    public void incrementUsedCount() {
        this.usedCount++;
        if (this.usedCount >= this.usageLimit) {
            this.isActive = false;
        }
    }
}