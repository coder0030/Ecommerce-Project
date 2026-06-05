package sumitproject.SpringCart.Mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.OrderDTO;
import sumitproject.SpringCart.DTO.OrderItemDTO;
import sumitproject.SpringCart.Entity.Address;
import sumitproject.SpringCart.Entity.Order;
import sumitproject.SpringCart.Entity.Payment;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderServiceMapper {

    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;

    public OrderDTO toDto(Order order, Payment payment) {
        if (order == null) return null;

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        orderDTO.setUserName(order.getUser() != null ? order.getUser().getName() : null);
        orderDTO.setCouponCode(order.getCoupon() != null ? order.getCoupon().getCode() : null);
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setDiscountAmount(order.getDiscountAmount());
        orderDTO.setFinalAmount(order.getFinalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());

        if (order.getOrderItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                    .map(orderItemMapper::toDto)
                    .collect(Collectors.toList());
            orderDTO.setOrderItems(itemDTOs);
        }

        if (payment != null) {
            orderDTO.setPayment(paymentMapper.toDto(payment));
        }

        return orderDTO;
    }
}