package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.PaymentDTO;
import sumitproject.SpringCart.RequestDTO.PaymentRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdatePaymentStatusRequestDTO;
import sumitproject.SpringCart.Service.PaymentService;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        return null;
    }

    @Override
    public Page<PaymentDTO> getAllPayments(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        return null;
    }

    @Override
    public PaymentDTO getPaymentByOrderId(Long orderId) {
        return null;
    }

    @Override
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        return null;
    }

    @Override
    public Page<PaymentDTO> getPaymentsByStatus(String status, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public PaymentDTO refundPayment(Long id) {
        return null;
    }

    @Override
    public PaymentDTO updatePaymentStatus(Long id, UpdatePaymentStatusRequestDTO statusRequest) {
        return null;
    }
}
