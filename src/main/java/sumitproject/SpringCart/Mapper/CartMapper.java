package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.DTO.CartItemDTO;
import sumitproject.SpringCart.Entity.Cart;
import sumitproject.SpringCart.Entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    public CartDTO toDto(Cart cart, User user, List<CartItemDTO> items) {
        if (cart == null) return null;

        CartDTO.CartDTOBuilder builder = CartDTO.builder()
                .id(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .updatedAt(cart.getUpdatedAt())
                .items(items);

        if (user != null) {
            builder.userId(user.getId());
        } else {
            builder.userId(cart.getUserId());
        }

        if (items != null) {
            builder.totalItems(items.stream()
                    .mapToInt(CartItemDTO::getQuantity)
                    .sum());
        }

        return builder.build();
    }

    public List<CartDTO> toDtoList(List<Cart> carts, User user, List<CartItemDTO> itemsList) {
        if (carts == null || carts.isEmpty()) return null;
        return carts.stream()
                .map(cart -> toDto(cart, user, null))
                .collect(Collectors.toList());
    }
}
