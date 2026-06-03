package sumitproject.SpringCart.Service;

import org.springframework.data.domain.Page;
import sumitproject.SpringCart.DTO.PaymentDTO;
import sumitproject.SpringCart.Entity.Order;
import sumitproject.SpringCart.Helper.PaymentMethod;
import sumitproject.SpringCart.RequestDTO.PaymentRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdatePaymentStatusRequestDTO;

public interface PaymentService {
    PaymentDTO processPayment(PaymentRequestDTO paymentRequestDTO);

    PaymentDTO processPaymentForOrder(Order order, PaymentMethod paymentMethod);
    PaymentDTO processSuccessfulPayment(Order order, PaymentMethod paymentMethod, String transactionId);
    PaymentDTO createPendingPayment(Order order, PaymentMethod paymentMethod);
    void updatePaymentToSuccess(Long orderId, String transactionId);
    void updatePaymentToFailed(Long orderId, String failureReason);
    void refundPaymentForOrder(Long orderId);
    boolean hasSuccessfulPayment(Long orderId);
    boolean isPaymentPending(Long orderId);

    Page<PaymentDTO> getAllPayments(int pageNo, int pageSize);
    PaymentDTO getPaymentById(Long id);
    PaymentDTO getPaymentByOrderId(Long orderId);
    PaymentDTO getPaymentByTransactionId(String transactionId);
    Page<PaymentDTO> getPaymentsByStatus(String status, int pageNo, int pageSize);
    PaymentDTO refundPayment(Long id);
    PaymentDTO updatePaymentStatus(Long id, UpdatePaymentStatusRequestDTO statusRequest);
}