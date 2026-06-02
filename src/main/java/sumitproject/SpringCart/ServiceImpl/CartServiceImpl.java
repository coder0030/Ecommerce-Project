package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.RequestDTO.CartItemRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateCartItemRequestDTO;
import sumitproject.SpringCart.Service.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    @Override
    public CartDTO createCart(Long userId) {
        return null;
    }

    @Override
    public CartDTO getCartById(Long id) {
        return null;
    }

    @Override
    public CartDTO getCartByUserId(Long userId) {
        return null;
    }

    @Override
    public CartDTO addItemToCart(Long cartId, CartItemRequestDTO cartItemRequestDTO) {
        return null;
    }

    @Override
    public CartDTO updateCartItem(Long cartId, UpdateCartItemRequestDTO updateRequest) {
        return null;
    }

    @Override
    public CartDTO removeItemFromCart(Long cartId, Long cartItemId) {
        return null;
    }

    @Override
    public void clearCart(Long cartId) {

    }

    @Override
    public double getCartTotal(Long cartId) {
        return 0;
    }
}
