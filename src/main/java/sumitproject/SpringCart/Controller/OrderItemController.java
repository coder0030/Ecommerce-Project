package sumitproject.SpringCart.Controller;

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
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
        OrderItemDTO orderItem = orderItemService.getOrderItemById(id);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }


    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#orderId, authentication)")
    public ResponseEntity<List<OrderItemDTO>> getItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDTO> orderItems = orderItemService.getItemsByOrderId(orderId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }


    @GetMapping("/order/{orderId}/paginated")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#orderId, authentication)")
    public ResponseEntity<Page<OrderItemDTO>> getItemsByOrderIdPaginated(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<OrderItemDTO> orderItems = orderItemService.getItemsByOrderIdPaginated(orderId, pageNo, pageSize);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }


    @GetMapping("/order/{orderId}/total")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#orderId, authentication)")
    public ResponseEntity<Double> calculateOrderTotal(@PathVariable Long orderId) {
        Double total = orderItemService.calculateOrderTotal(orderId);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @DeleteMapping("/{orderItemId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.orderVerify(#orderId, authentication)")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}