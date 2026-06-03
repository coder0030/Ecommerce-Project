package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.PaymentDTO;
import sumitproject.SpringCart.Entity.Order;
import sumitproject.SpringCart.Entity.Payment;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.RequestDTO.PaymentRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    public PaymentDTO toDto(Payment payment) {
        if (payment == null) return null;

        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaidAt(payment.getPaidAt());

        if (payment.getOrder() != null) {
            dto.setOrderId(payment.getOrder().getId());
        }

        return dto;
    }

    public Payment toEntity(PaymentRequestDTO paymentDTO, Payment payment) {
        if (paymentDTO == null) return null;

        if (paymentDTO.getPaymentMethod() != null) {
            payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        }
        if (paymentDTO.getTransactionId() != null) {
            payment.setTransactionId(paymentDTO.getTransactionId());
        }
        payment.setAmount(paymentDTO.getAmount());

        return payment;
    }


    public Payment updateToEntity(PaymentRequestDTO paymentDTO, Payment payment) {
        boolean nullValue = false;

        if (paymentDTO.getPaymentMethod() == null) nullValue = true;

        if (nullValue) {
            throw new BadRequestException("Incomplete data provided. Amount and payment method are required.");
        }

        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        if (paymentDTO.getTransactionId() != null) {
            payment.setTransactionId(paymentDTO.getTransactionId());
        }

        return payment;
    }
}
