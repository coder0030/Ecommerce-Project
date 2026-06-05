package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carts", description = "Manage shopping carts for users")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Create a new cart", description = "Creates a new shopping cart for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cart created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CartDTO> createCart(@Valid @RequestBody CartRequestDTO request) {
        CartDTO createdCart = cartService.createCart(request);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @Operation(summary = "Get cart by ID", description = "Retrieves a specific cart by its ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long id) {
        CartDTO cart = cartService.getCartById(id);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Get cart by user ID", description = "Retrieves cart for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Get cart with items", description = "Retrieves cart details along with all cart items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{cartId}/with-items")
    @PreAuthorize("hasRole('ADMIN') or @cartOwnerCheck.cartVerify(#cartId, authentication)")
    public ResponseEntity<CartDTO> getCartWithItems(@PathVariable Long cartId) {
        CartDTO cart = cartService.getCartWithItems(cartId);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Clear cart", description = "Removes all items from a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{cartId}/clear")
    @PreAuthorize("hasRole('ADMIN') or @cartOwnerCheck.cartVerify(#cartId, authentication)")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get cart total", description = "Calculates total price of all items in cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{cartId}/total")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<Double> getCartTotal(@PathVariable Long cartId) {
        double total = cartService.getCartTotal(cartId);
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Reactivate cart", description = "Reactivates an inactive cart for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart reactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @PostMapping("/reactivate/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<CartDTO> reactivateCart(@PathVariable Long userId) {
        CartDTO reactivatedCart = cartService.reactivateCart(userId);
        return ResponseEntity.ok(reactivatedCart);
    }

    @Operation(summary = "Delete cart", description = "Permanently deletes a cart (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{cartId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}