package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.RequestDTO.CartRequestDTO;

@Component
public interface CartService {

    CartDTO createCart(CartRequestDTO request);

    CartDTO getCartById(Long id);

    CartDTO getCartByUserId(Long userId);

    CartDTO getCartWithItems(Long cartId);

    void clearCart(Long cartId);

    double getCartTotal(Long cartId);

    CartDTO reactivateCart(Long userId);

    void deleteCart(Long cartId);
}