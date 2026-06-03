package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CartItemDTO;
import sumitproject.SpringCart.Entity.CartItem;
import sumitproject.SpringCart.Entity.Product;

@Component
public class CartItemMapper {

    public CartItemDTO toDto(CartItem cartItem) {
        if (cartItem == null) return null;

        CartItemDTO.CartItemDTOBuilder builder = CartItemDTO.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .addedAt(cartItem.getAddedAt())
                .subtotal(cartItem.getPrice() * cartItem.getQuantity());

        if (cartItem.getCart() != null) {
            builder.cartId(cartItem.getCart().getId());
        }

        if (cartItem.getProduct() != null) {
            Product product = cartItem.getProduct();
            builder.productId(product.getId());
            builder.productName(product.getName());
            builder.productImage(product.getImageUrl());
        }

        return builder.build();
    }
}