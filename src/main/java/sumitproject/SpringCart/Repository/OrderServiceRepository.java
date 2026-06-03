package sumitproject.SpringCart.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.Order;
import sumitproject.SpringCart.Helper.OrderStatus;

import java.util.List;

public interface OrderServiceRepository extends JpaRepository<Order, Long> {

    Page<Order> findByStatus(OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByOrderItems_Order_Id(Long userId, Pageable pageable);
}