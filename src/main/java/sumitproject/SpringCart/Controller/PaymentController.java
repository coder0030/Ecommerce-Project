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
import sumitproject.SpringCart.DTO.PaymentDTO;
import sumitproject.SpringCart.RequestDTO.PaymentRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdatePaymentStatusRequestDTO;
import sumitproject.SpringCart.Service.PaymentService;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Manage payment processing and transactions")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Process payment", description = "Processes a payment for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment details"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentDTO response = paymentService.processPayment(paymentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all payments", description = "Retrieves paginated list of all payments (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<PaymentDTO> payments = paymentService.getAllPayments(pageNo, pageSize);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @Operation(summary = "Get payment by ID", description = "Retrieves a specific payment by its ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get payment by order ID", description = "Retrieves payment details for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @orderOwnerCheck.verify(#orderId, authentication)")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        PaymentDTO paymentDTO = paymentService.getPaymentByOrderId(orderId);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get payment by transaction ID", description = "Retrieves payment by transaction ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        PaymentDTO paymentDTO = paymentService.getPaymentByTransactionId(transactionId);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get payments by status", description = "Retrieves all payments with a specific status (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PaymentDTO>> getPaymentsByStatus(@PathVariable String status,
                                                                @RequestParam(defaultValue = "0") int pageNo,
                                                                @RequestParam(defaultValue = "20") int pageSize) {
        Page<PaymentDTO> payments = paymentService.getPaymentsByStatus(status, pageNo, pageSize);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @Operation(summary = "Update payment status", description = "Updates the status of a payment (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable Long id,
                                                          @Valid @RequestBody UpdatePaymentStatusRequestDTO statusRequest) {
        PaymentDTO updatedPayment = paymentService.updatePaymentStatus(id, statusRequest);
        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
    }

    @Operation(summary = "Refund payment", description = "Processes a refund for a payment (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment refunded successfully"),
            @ApiResponse(responseCode = "400", description = "Payment cannot be refunded"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> refundPayment(@PathVariable Long id) {
        PaymentDTO refundedPayment = paymentService.refundPayment(id);
        return new ResponseEntity<>(refundedPayment, HttpStatus.OK);
    }

    @Operation(summary = "Mark payment as success", description = "Updates payment status to successful (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment marked as successful"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}/success")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> markPaymentSuccess(
            @PathVariable Long orderId,
            @RequestParam String transactionId) {

        paymentService.updatePaymentToSuccess(orderId, transactionId);

        return ResponseEntity.ok("Payment marked as successful.");
    }

    @Operation(summary = "Mark payment as failed", description = "Updates payment status to failed (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment marked as failed"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}/failed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> markPaymentFailed(
            @PathVariable Long orderId,
            @RequestParam String failureReason) {

        paymentService.updatePaymentToFailed(orderId, failureReason);

        return ResponseEntity.ok("Payment marked as failed.");
    }

    @Operation(summary = "Check pending payment", description = "Checks if payment is pending for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Boolean> isPaymentPending(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.isPaymentPending(orderId)
        );
    }
}