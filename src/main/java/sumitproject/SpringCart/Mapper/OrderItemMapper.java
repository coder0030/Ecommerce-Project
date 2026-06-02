package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.OrderItemDTO;
import sumitproject.SpringCart.Entity.OrderItem;
import sumitproject.SpringCart.Entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {

    public OrderItemDTO toDto(OrderItem orderItem, Product product) {
        if (orderItem == null) return null;

        OrderItemDTO.OrderItemDTOBuilder builder = OrderItemDTO.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrderId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .priceAtPurchase(orderItem.getPriceAtPurchase());

        if (product != null) {
            builder.productName(product.getName())
                    .productImage(product.getImageUrl());
        }

        return builder.build();
    }

    public List<OrderItemDTO> toDtoList(List<OrderItem> orderItems, Product product) {
        if (orderItems == null || orderItems.isEmpty()) return null;
        return orderItems.stream()
                .map(item -> toDto(item, product))
                .collect(Collectors.toList());
    }
}
