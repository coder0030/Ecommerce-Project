package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.CartItemDTO;
import sumitproject.SpringCart.RequestDTO.CartItemRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateCartItemRequestDTO;
import sumitproject.SpringCart.Service.CartItemService;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable Long id) {
        CartItemDTO cartItem = cartItemService.getCartItemById(id);
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<CartItemDTO> addItemToCart(
            @PathVariable Long cartId,
            @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        CartItemDTO addedItem = cartItemService.addItemToCart(cartId, cartItemRequestDTO);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @PostMapping("/cart/{cartId}/bulk")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<List<CartItemDTO>> addItemsToCartBulk(
            @PathVariable Long cartId,
            @Valid @RequestBody List<CartItemRequestDTO> cartItemRequestDTOs) {
        List<CartItemDTO> addedItems = cartItemService.addItemsToCartBulk(cartId, cartItemRequestDTOs);
        return new ResponseEntity<>(addedItems, HttpStatus.CREATED);
    }

    @PutMapping("/{cartItemId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.verifyItem(#updateRequest.cartItemId, authentication)")
    public ResponseEntity<CartItemDTO> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequestDTO updateRequest) {
        updateRequest.setCartItemId(cartItemId);
        CartItemDTO updatedItem = cartItemService.updateCartItem(updateRequest);
        return ResponseEntity.ok(updatedItem);
    }

    @PutMapping("/{cartItemId}/quantity")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CartItemDTO> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        CartItemDTO updatedItem = cartItemService.updateCartItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{cartItemId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.verifyItem(#id, authentication)")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.deleteCartItemById(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<Page<CartItemDTO>> getCartItemsByCartId(
            @PathVariable Long cartId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CartItemDTO> cartItems = cartItemService.getCartItemsByCartId(cartId, page, size);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/cart/{cartId}/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<CartItemDTO> getCartItemByCartAndProduct(
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        CartItemDTO cartItem = cartItemService.getCartItemByCartAndProduct(cartId, productId);
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/cart/{cartId}/all")
    @PreAuthorize("hasRole('ADMIN') or @cartOwnerCheck.verify(#cartId, authentication)")
    public ResponseEntity<Void> deleteAllCartItemsByCartId(@PathVariable Long cartId) {
        cartItemService.deleteAllCartItemsByCartId(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cart/{cartId}/count")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<Integer> getCartItemCountByCartId(@PathVariable Long cartId) {
        Integer count = cartItemService.getCartItemCountByCartId(cartId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{cartItemId}/subtotal")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.verifyItem(#id, authentication)")
    public ResponseEntity<Double> getCartItemSubtotal(@PathVariable Long cartItemId) {
        Double subtotal = cartItemService.getCartItemSubtotal(cartItemId);
        return ResponseEntity.ok(subtotal);
    }

    @GetMapping("/cart/{cartId}/product/{productId}/exists")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<Boolean> isProductInCart(
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        Boolean exists = cartItemService.isProductInCart(cartId, productId);
        return ResponseEntity.ok(exists);
    }
}