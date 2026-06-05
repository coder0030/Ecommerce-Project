package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.OrderItemDTO;
import sumitproject.SpringCart.Service.OrderItemService;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@Tag(name = "Order Items", description = "Manage items within customer orders")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Operation(summary = "Get order item by ID", description = "Retrieves a specific order item (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
        OrderItemDTO orderItem = orderItemService.getOrderItemById(id);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }

    @Operation(summary = "Get items by order ID", description = "Retrieves all items for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#orderId, authentication)")
    public ResponseEntity<List<OrderItemDTO>> getItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDTO> orderItems = orderItemService.getItemsByOrderId(orderId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @Operation(summary = "Get items by order ID (paginated)", description = "Retrieves paginated items for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/order/{orderId}/paginated")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#orderId, authentication)")
    public ResponseEntity<Page<OrderItemDTO>> getItemsByOrderIdPaginated(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<OrderItemDTO> orderItems = orderItemService.getItemsByOrderIdPaginated(orderId, pageNo, pageSize);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @Operation(summary = "Calculate order total", description = "Calculates total amount for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/order/{orderId}/total")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#orderId, authentication)")
    public ResponseEntity<Double> calculateOrderTotal(@PathVariable Long orderId) {
        Double total = orderItemService.calculateOrderTotal(orderId);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @Operation(summary = "Delete order item", description = "Removes a specific item from an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @DeleteMapping("/{orderItemId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#orderId, authentication)")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}