package sumitproject.SpringCart.Service;

import org.springframework.data.domain.Page;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.DTO.CartItemDTO;
import sumitproject.SpringCart.RequestDTO.CartItemRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateCartItemRequestDTO;

import java.util.List;

public interface CartItemService {

    CartItemDTO getCartItemById(Long id);

    CartItemDTO addItemToCart(Long cartId, CartItemRequestDTO cartItemRequestDTO);

    CartItemDTO updateCartItem(UpdateCartItemRequestDTO updateRequest);

    CartItemDTO updateCartItemQuantity(Long cartItemId, Integer quantity);

    void deleteCartItemById(Long id);

    List<CartItemDTO> addItemsToCartBulk(Long cartId, List<CartItemRequestDTO> cartItemRequestDTOs);

    void deleteAllCartItemsByCartId(Long cartId);

    Page<CartItemDTO> getCartItemsByCartId(Long cartId, int pageNo, int pageSize);

    CartItemDTO getCartItemByCartAndProduct(Long cartId, Long productId);

    Integer getCartItemCountByCartId(Long cartId);

    Double getCartItemSubtotal(Long id);

    Boolean isProductInCart(Long cartId, Long productId);
}