package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentDTO response = paymentService.processPayment(paymentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<PaymentDTO> payments = paymentService.getAllPayments(pageNo, pageSize);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        PaymentDTO paymentDTO = paymentService.getPaymentByOrderId(orderId);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        PaymentDTO paymentDTO = paymentService.getPaymentByTransactionId(transactionId);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PaymentDTO>> getPaymentsByStatus(@PathVariable String status,
                                                                @RequestParam(defaultValue = "0") int pageNo,
                                                                @RequestParam(defaultValue = "20") int pageSize) {
        Page<PaymentDTO> payments = paymentService.getPaymentsByStatus(status, pageNo, pageSize);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable Long id,
                                                          @Valid @RequestBody UpdatePaymentStatusRequestDTO statusRequest) {
        PaymentDTO updatedPayment = paymentService.updatePaymentStatus(id, statusRequest);
        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentDTO> refundPayment(@PathVariable Long id) {
        PaymentDTO refundedPayment = paymentService.refundPayment(id);
        return new ResponseEntity<>(refundedPayment, HttpStatus.OK);
    }
}
