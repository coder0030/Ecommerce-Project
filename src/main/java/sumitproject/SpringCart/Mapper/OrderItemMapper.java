package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.OrderItemDTO;
import sumitproject.SpringCart.Entity.OrderItem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {

    public OrderItemDTO toDto(OrderItem orderItem) {
        if (orderItem == null) return null;

        OrderItemDTO dto = new OrderItemDTO();

        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPriceAtPurchase(orderItem.getPriceAtPurchase());

        dto.setSubtotal(orderItem.getSubtotal());

        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
            dto.setProductName(orderItem.getProduct().getName());
            dto.setProductImage(orderItem.getProduct().getImageUrl());
        }

        if (orderItem.getOrder() != null) {
            dto.setOrderId(orderItem.getOrder().getId());
        }

        return dto;
    }

    public List<OrderItemDTO> toDtoList(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) return Collections.emptyList();
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OrderItem toEntity(sumitproject.SpringCart.Entity.Order order,
                              sumitproject.SpringCart.Entity.CartItem cartItem) {
        if (cartItem == null) return null;

        return OrderItem.builder()
                .order(order)
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .priceAtPurchase(cartItem.getPrice())
                .build();
    }
}