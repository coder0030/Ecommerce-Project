package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.OrderDTO;
import sumitproject.SpringCart.RequestDTO.OrderRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateOrderStatusRequestDTO;
import sumitproject.SpringCart.Service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Override
    public OrderDTO createOrder(Long userId, OrderRequestDTO orderRequestDTO) {
        return null;
    }

    @Override
    public Page<OrderDTO> getAllOrders(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        return null;
    }

    @Override
    public Page<OrderDTO> getOrdersByUserId(Long userId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<OrderDTO> getOrdersByStatus(String status, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, UpdateOrderStatusRequestDTO statusRequest) {
        return null;
    }

    @Override
    public OrderDTO cancelOrder(Long id, String reason) {
        return null;
    }

    @Override
    public void deleteOrderById(Long id) {

    }
}
