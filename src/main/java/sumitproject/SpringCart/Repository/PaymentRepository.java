package sumitproject.SpringCart.Repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.Payment;
import sumitproject.SpringCart.Helper.PaymentStatus;

import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder_Id(Long id);

    boolean existsByOrder_Id(@NotNull(message = "Order ID is required") Long orderId);

    Optional<Payment> findByTransactionId(String transactionId);

    Page<Payment> findByStatus(PaymentStatus paymentStatus, Pageable pageable);

    Optional<Payment> findByOrder_IdAndStatus(Long orderId, PaymentStatus paymentStatus);
}
