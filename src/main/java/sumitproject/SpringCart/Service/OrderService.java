package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.OrderDTO;
import sumitproject.SpringCart.RequestDTO.AddressUpdateRequestDTO;
import sumitproject.SpringCart.RequestDTO.OrderRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateOrderStatusRequestDTO;

@Component
public interface OrderService {
    OrderDTO createOrder(Long userId, @Valid OrderRequestDTO orderRequestDTO);

    Page<OrderDTO> getAllOrders(int pageNo, int pageSize);

    OrderDTO getOrderById(Long id);

    Page<OrderDTO> getOrdersByUserId(Long userId, int pageNo, int pageSize);

    Page<OrderDTO> getOrdersByStatus(String status, int pageNo, int pageSize);

    OrderDTO updateOrderStatus(Long id, @Valid UpdateOrderStatusRequestDTO statusRequest);

    OrderDTO cancelOrder(Long id, String reason);

    void deleteOrderById(Long id);

    void deleteAddressById(Long userId, AddressUpdateRequestDTO request);

    void updateAddress(Long userId, AddressUpdateRequestDTO request);
}
