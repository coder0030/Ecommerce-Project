package sumitproject.SpringCart.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_Id(Long orderId);

    Page<OrderItem> findByOrder_Id(Long orderId, Pageable pageable);

    List<OrderItem> findByProduct_Id(Long productId);

    boolean existsByOrder_IdAndProduct_Id(Long orderId, Long productId);

    Integer countByOrder_Id(Long orderId);
}