package sumitproject.SpringCart.Repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.Order;
import sumitproject.SpringCart.Helper.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderServiceRepository extends JpaRepository<Order, Long> {

    Page<Order> findByStatus(OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByOrderItems_Order_Id(Long userId, Pageable pageable);

    boolean existsByIdAndUser_IdAndUser_IsActiveTrue(Long orderId, Long id);

    boolean existsByAddress_IdAndAddress_IsActiveTrueAndStatus(Long id, OrderStatus orderStatus);

    boolean existsByAddress_IdAndAddress_IsActiveTrueAndStatusIn(Long id, Set<OrderStatus> pending);

    Page<Order> findByUser_Id(Long userId, Pageable pageable);

    Order findByIdAndStatusIn(@NotNull(message = "User ID is required") Long orderId, Set<OrderStatus> pending);
}