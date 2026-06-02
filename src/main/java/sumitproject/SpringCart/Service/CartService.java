package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.RequestDTO.CartItemRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateCartItemRequestDTO;

@Component
public interface CartService {
    CartDTO createCart(Long userId);

    CartDTO getCartById(Long id);

    CartDTO getCartByUserId(Long userId);

    CartDTO addItemToCart(Long cartId, @Valid CartItemRequestDTO cartItemRequestDTO);

    CartDTO updateCartItem(Long cartId, @Valid UpdateCartItemRequestDTO updateRequest);

    CartDTO removeItemFromCart(Long cartId, Long cartItemId);

    void clearCart(Long cartId);

    double getCartTotal(Long cartId);
}
