package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.RequestDTO.CartRequestDTO;
import sumitproject.SpringCart.Service.CartService;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CartDTO> createCart(@Valid @RequestBody CartRequestDTO request) {
        CartDTO createdCart = cartService.createCart(request);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long id) {
        CartDTO cart = cartService.getCartById(id);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{cartId}/with-items")
    @PreAuthorize("hasRole('ADMIN') or @cartOwnerCheck.cartVerify(#cartId, authentication)")
    public ResponseEntity<CartDTO> getCartWithItems(@PathVariable Long cartId) {
        CartDTO cart = cartService.getCartWithItems(cartId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}/clear")
    @PreAuthorize("hasRole('ADMIN') or @cartOwnerCheck.cartVerify(#cartId, authentication)")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/total")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")    public ResponseEntity<Double> getCartTotal(@PathVariable Long cartId) {
        double total = cartService.getCartTotal(cartId);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/reactivate/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<CartDTO> reactivateCart(@PathVariable Long userId) {
        CartDTO reactivatedCart = cartService.reactivateCart(userId);
        return ResponseEntity.ok(reactivatedCart);
    }

    @DeleteMapping("/{cartId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}