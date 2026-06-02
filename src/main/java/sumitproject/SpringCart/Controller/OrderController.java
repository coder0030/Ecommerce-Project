package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.OrderDTO;
import sumitproject.SpringCart.RequestDTO.OrderRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateOrderStatusRequestDTO;
import sumitproject.SpringCart.Service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long userId,
                                                @Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO createdOrder = orderService.createOrder(userId, orderRequestDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<OrderDTO> orders = orderService.getAllOrders(pageNo, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByUserId(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "0") int pageNo,
                                                            @RequestParam(defaultValue = "20") int pageSize) {
        Page<OrderDTO> orders = orderService.getOrdersByUserId(userId, pageNo, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByStatus(@PathVariable String status,
                                                            @RequestParam(defaultValue = "0") int pageNo,
                                                            @RequestParam(defaultValue = "20") int pageSize) {
        Page<OrderDTO> orders = orderService.getOrdersByStatus(status, pageNo, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateOrderStatusRequestDTO statusRequest) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, statusRequest);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id,
                                                @RequestParam(required = false) String reason) {
        OrderDTO cancelledOrder = orderService.cancelOrder(id, reason);
        return new ResponseEntity<>(cancelledOrder, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
