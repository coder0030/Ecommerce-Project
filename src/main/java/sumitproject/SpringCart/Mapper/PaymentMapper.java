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

    public PaymentDTO toDto(Payment payment, Order order) {
        if (payment == null) return null;

        PaymentDTO.PaymentDTOBuilder builder = PaymentDTO.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .paidAt(payment.getPaidAt());

        if (order != null) {
            builder.orderId(order.getId())
                    .orderNumber("ORD-" + order.getId());
        } else {
            builder.orderId(payment.getOrderId());
        }

        return builder.build();
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

    public List<PaymentDTO> toDtoList(List<Payment> payments, List<Order> orders) {
        if (payments == null || payments.isEmpty()) return null;

        return payments.stream()
                .map(payment -> {
                    Order order = orders != null ?
                            orders.stream()
                            .filter(o -> o.getId().equals(payment.getOrderId()))
                            .findFirst()
                            .orElse(null) : null;
                    return toDto(payment, order);
                })
                .collect(Collectors.toList());
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
