package sumitproject.SpringCart.Service;

import org.springframework.data.domain.Page;
import sumitproject.SpringCart.DTO.OrderItemDTO;

import java.util.List;

public interface OrderItemService {

    OrderItemDTO getOrderItemById(Long id);

    List<OrderItemDTO> getItemsByOrderId(Long orderId);

    Page<OrderItemDTO> getItemsByOrderIdPaginated(Long orderId, int pageNo, int pageSize);

    Double calculateOrderTotal(Long orderId);

    void deleteOrderItem(Long orderItemId);
}