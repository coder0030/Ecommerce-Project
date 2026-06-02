package sumitproject.SpringCart.Entity;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;
import sumitproject.SpringCart.Helper.PaymentMethod;
import sumitproject.SpringCart.Helper.PaymentStatus;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true)
    private Long orderId;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING','SUCCESS','FAILED') DEFAULT 'PENDING'")
    private PaymentStatus status;

    @Column(name = "transaction_id", unique = true, length = 100)
    private String transactionId;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

}
