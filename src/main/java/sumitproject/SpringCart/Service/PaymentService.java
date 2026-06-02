package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.PaymentDTO;
import sumitproject.SpringCart.RequestDTO.PaymentRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdatePaymentStatusRequestDTO;

@Component
public interface PaymentService {
    PaymentDTO processPayment(@Valid PaymentRequestDTO paymentRequestDTO);

    Page<PaymentDTO> getAllPayments(int pageNo, int pageSize);

    PaymentDTO getPaymentById(Long id);

    PaymentDTO getPaymentByOrderId(Long orderId);

    PaymentDTO getPaymentByTransactionId(String transactionId);

    Page<PaymentDTO> getPaymentsByStatus(String status, int pageNo, int pageSize);
    
    PaymentDTO refundPayment(Long id);

    PaymentDTO updatePaymentStatus(Long id, @Valid UpdatePaymentStatusRequestDTO statusRequest);
}
