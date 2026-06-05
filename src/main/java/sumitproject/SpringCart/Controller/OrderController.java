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
import sumitproject.SpringCart.DTO.OrderDTO;
import sumitproject.SpringCart.RequestDTO.AddressUpdateRequestDTO;
import sumitproject.SpringCart.RequestDTO.OrderRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateOrderStatusRequestDTO;
import sumitproject.SpringCart.Service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Manage customer orders and order status")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create order", description = "Creates a new order for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/create/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long userId,
                                                @Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO createdOrder = orderService.createOrder(userId, orderRequestDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all orders", description = "Retrieves paginated list of all orders (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<OrderDTO> orders = orderService.getAllOrders(pageNo, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(summary = "Get order by ID", description = "Retrieves a specific order by its ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get orders by user", description = "Retrieves all orders for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Page<OrderDTO>> getOrdersByUserId(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "0") int pageNo,
                                                            @RequestParam(defaultValue = "20") int pageSize) {
        Page<OrderDTO> orders = orderService.getOrdersByUserId(userId, pageNo, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(summary = "Get orders by status", description = "Retrieves all orders with a specific status (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderDTO>> getOrdersByStatus(@PathVariable String status,
                                                            @RequestParam(defaultValue = "0") int pageNo,
                                                            @RequestParam(defaultValue = "20") int pageSize) {
        Page<OrderDTO> orders = orderService.getOrdersByStatus(status, pageNo, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(summary = "Update order status", description = "Updates the status of an order (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateOrderStatusRequestDTO statusRequest) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, statusRequest);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @Operation(summary = "Cancel order", description = "Cancels an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#id, authentication)")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id,
                                                @RequestParam(required = false) String reason) {
        OrderDTO cancelledOrder = orderService.cancelOrder(id, reason);
        return new ResponseEntity<>(cancelledOrder, HttpStatus.OK);
    }

    @Operation(summary = "Delete order", description = "Permanently deletes an order (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update user address", description = "Updates address for authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address details"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or #userId == authenticated.getPrincipal.getId")
    public ResponseEntity<String> updateAddress(@PathVariable Long userId, @RequestBody AddressUpdateRequestDTO request) {
        orderService.updateAddress(userId, request);
        return new ResponseEntity<>("Address updated successfully", HttpStatus.OK);
    }

    @Operation(summary = "Delete address", description = "Permanently deletes an address (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authenticated.getPrincipal.getId")
    public ResponseEntity<Void> deleteAddressById(@PathVariable Long userId, @RequestBody AddressUpdateRequestDTO request) {
        orderService.deleteAddressById(userId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}