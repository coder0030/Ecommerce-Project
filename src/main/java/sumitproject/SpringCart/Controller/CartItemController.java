package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cart Items", description = "Manage items inside shopping carts")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartItemController {

    private final CartItemService cartItemService;

    @Operation(summary = "Get cart item by ID", description = "Retrieves a specific cart item (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable Long id) {
        CartItemDTO cartItem = cartItemService.getCartItemById(id);
        return ResponseEntity.ok(cartItem);
    }

    @Operation(summary = "Add item to cart", description = "Adds a single item to the specified cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })
    @PostMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<CartItemDTO> addItemToCart(
            @PathVariable Long cartId,
            @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        CartItemDTO addedItem = cartItemService.addItemToCart(cartId, cartItemRequestDTO);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @Operation(summary = "Add multiple items to cart", description = "Adds multiple items to the specified cart in bulk")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Items added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })
    @PostMapping("/cart/{cartId}/bulk")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<List<CartItemDTO>> addItemsToCartBulk(
            @PathVariable Long cartId,
            @Valid @RequestBody List<CartItemRequestDTO> cartItemRequestDTOs) {
        List<CartItemDTO> addedItems = cartItemService.addItemsToCartBulk(cartId, cartItemRequestDTOs);
        return new ResponseEntity<>(addedItems, HttpStatus.CREATED);
    }

    @Operation(summary = "Update cart item", description = "Updates an existing cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/{cartItemId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.verifyItem(#updateRequest.cartItemId, authentication)")
    public ResponseEntity<CartItemDTO> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequestDTO updateRequest) {
        updateRequest.setCartItemId(cartItemId);
        CartItemDTO updatedItem = cartItemService.updateCartItem(updateRequest);
        return ResponseEntity.ok(updatedItem);
    }

    @Operation(summary = "Update cart item quantity", description = "Updates only the quantity of a cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/{cartItemId}/quantity")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CartItemDTO> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        CartItemDTO updatedItem = cartItemService.updateCartItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    @Operation(summary = "Delete cart item", description = "Removes a specific item from cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{cartItemId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.verifyItem(#id, authentication)")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.deleteCartItemById(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all items in cart", description = "Retrieves paginated list of items in a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<Page<CartItemDTO>> getCartItemsByCartId(
            @PathVariable Long cartId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CartItemDTO> cartItems = cartItemService.getCartItemsByCartId(cartId, page, size);
        return ResponseEntity.ok(cartItems);
    }

    @Operation(summary = "Get cart item by cart and product", description = "Retrieves a specific cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @GetMapping("/cart/{cartId}/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<CartItemDTO> getCartItemByCartAndProduct(
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        CartItemDTO cartItem = cartItemService.getCartItemByCartAndProduct(cartId, productId);
        return ResponseEntity.ok(cartItem);
    }

    @Operation(summary = "Delete all cart items", description = "Removes all items from a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All items deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/cart/{cartId}/all")
    @PreAuthorize("hasRole('ADMIN') or @cartOwnerCheck.verify(#cartId, authentication)")
    public ResponseEntity<Void> deleteAllCartItemsByCartId(@PathVariable Long cartId) {
        cartItemService.deleteAllCartItemsByCartId(cartId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get cart item count", description = "Returns total number of items in a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/cart/{cartId}/count")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<Integer> getCartItemCountByCartId(@PathVariable Long cartId) {
        Integer count = cartItemService.getCartItemCountByCartId(cartId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get cart item subtotal", description = "Calculates subtotal for a single cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subtotal calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @GetMapping("/{cartItemId}/subtotal")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.verifyItem(#id, authentication)")
    public ResponseEntity<Double> getCartItemSubtotal(@PathVariable Long cartItemId) {
        Double subtotal = cartItemService.getCartItemSubtotal(cartItemId);
        return ResponseEntity.ok(subtotal);
    }

    @Operation(summary = "Check if product is in cart", description = "Checks whether a specific product exists in the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/cart/{cartId}/product/{productId}/exists")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
    public ResponseEntity<Boolean> isProductInCart(
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        Boolean exists = cartItemService.isProductInCart(cartId, productId);
        return ResponseEntity.ok(exists);
    }
}