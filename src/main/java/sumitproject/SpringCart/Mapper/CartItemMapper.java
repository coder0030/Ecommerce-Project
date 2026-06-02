package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CartItemDTO;
import sumitproject.SpringCart.Entity.CartItem;
import sumitproject.SpringCart.Entity.Product;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.RequestDTO.CartItemRequestDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {

    public CartItemDTO toDto(CartItem cartItem, Product product) {
        if (cartItem == null) return null;

        CartItemDTO.CartItemDTOBuilder builder = CartItemDTO.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCartId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .addedAt(cartItem.getAddedAt());

        if (product != null) {
            builder.productName(product.getName())
                    .productImage(product.getImageUrl());
        }

        return builder.build();
    }

    public CartItem toEntity(CartItemRequestDTO cartItemDTO, CartItem cartItem) {
        if (cartItemDTO == null) return null;

        if (cartItemDTO.getQuantity() != null) {
            cartItem.setQuantity(cartItemDTO.getQuantity());
        }

        return cartItem;
    }

    public List<CartItemDTO> toDtoList(List<CartItem> cartItems, Product product) {
        if (cartItems == null || cartItems.isEmpty()) return null;
        return cartItems.stream()
                .map(item -> toDto(item, product))
                .collect(Collectors.toList());
    }

    public CartItem updateToEntity(CartItemRequestDTO cartItemDTO, CartItem cartItem) {
        boolean nullValue = false;

        if (cartItemDTO.getProductId() == null) nullValue = true;
        if (cartItemDTO.getQuantity() == null) nullValue = true;

        if (nullValue) {
            throw new BadRequestException("Incomplete data provided. Product ID and quantity are required.");
        }

        cartItem.setQuantity(cartItemDTO.getQuantity());

        return cartItem;
    }
}
