package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.RequestDTO.CartItemRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateCartItemRequestDTO;
import sumitproject.SpringCart.Service.CartService;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<CartDTO> createCart(@PathVariable Long userId) {
        CartDTO createdCart = cartService.createCart(userId);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long id) {
        CartDTO cartDTO = cartService.getCartById(id);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PostMapping("/{cartId}/add-item")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long cartId,
                                                 @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        CartDTO updatedCart = cartService.addItemToCart(cartId, cartItemRequestDTO);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @PutMapping("/{cartId}/update-item")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable Long cartId,
                                                  @Valid @RequestBody UpdateCartItemRequestDTO updateRequest) {
        CartDTO updatedCart = cartService.updateCartItem(cartId, updateRequest);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/remove-item/{cartItemId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long cartId,
                                                      @PathVariable Long cartItemId) {
        CartDTO updatedCart = cartService.removeItemFromCart(cartId, cartItemId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<Double> getCartTotal(@PathVariable Long cartId) {
        double total = cartService.getCartTotal(cartId);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }
}
