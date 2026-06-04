package sumitproject.SpringCart.Controller;

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
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentDTO response = paymentService.processPayment(paymentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<PaymentDTO> payments = paymentService.getAllPayments(pageNo, pageSize);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @orderOwnerCheck.verify(#orderId, authentication)")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        PaymentDTO paymentDTO = paymentService.getPaymentByOrderId(orderId);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        PaymentDTO paymentDTO = paymentService.getPaymentByTransactionId(transactionId);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PaymentDTO>> getPaymentsByStatus(@PathVariable String status,
                                                                @RequestParam(defaultValue = "0") int pageNo,
                                                                @RequestParam(defaultValue = "20") int pageSize) {
        Page<PaymentDTO> payments = paymentService.getPaymentsByStatus(status, pageNo, pageSize);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable Long id,
                                                          @Valid @RequestBody UpdatePaymentStatusRequestDTO statusRequest) {
        PaymentDTO updatedPayment = paymentService.updatePaymentStatus(id, statusRequest);
        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> refundPayment(@PathVariable Long id) {
        PaymentDTO refundedPayment = paymentService.refundPayment(id);
        return new ResponseEntity<>(refundedPayment, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/success")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> markPaymentSuccess(
            @PathVariable Long orderId,
            @RequestParam String transactionId) {

        paymentService.updatePaymentToSuccess(orderId, transactionId);

        return ResponseEntity.ok("Payment marked as successful.");
    }

    @PutMapping("/{orderId}/failed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> markPaymentFailed(
            @PathVariable Long orderId,
            @RequestParam String failureReason) {

        paymentService.updatePaymentToFailed(orderId, failureReason);

        return ResponseEntity.ok("Payment marked as failed.");
    }

    @GetMapping("/{orderId}/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Boolean> isPaymentPending(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.isPaymentPending(orderId)
        );
    }
}
