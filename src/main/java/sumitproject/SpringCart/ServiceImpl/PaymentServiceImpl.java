package sumitproject.SpringCart.ServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.AppConfig.AppConfig;
import sumitproject.SpringCart.DTO.PaymentDTO;
import sumitproject.SpringCart.Entity.Order;
import sumitproject.SpringCart.Entity.Payment;
import sumitproject.SpringCart.Helper.PaymentMethod;
import sumitproject.SpringCart.Helper.PaymentStatus;
import sumitproject.SpringCart.Mapper.PaymentMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DataNotFoundException;
import sumitproject.SpringCart.Repository.OrderServiceRepository;
import sumitproject.SpringCart.Repository.PaymentRepository;
import sumitproject.SpringCart.RequestDTO.PaymentRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdatePaymentStatusRequestDTO;
import sumitproject.SpringCart.Service.PaymentService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderServiceRepository orderRepository;
    private final AppConfig appConfig;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        Order order = orderRepository.findById(paymentRequestDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + paymentRequestDTO.getOrderId()));

        if (paymentRepository.existsByOrder_Id(paymentRequestDTO.getOrderId())) {
            throw new BadRequestException("Payment already processed for order id: " + paymentRequestDTO.getOrderId());
        }

        Payment payment = createPayment(order, paymentRequestDTO);
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentDTO processPaymentForOrder(Order order, PaymentMethod paymentMethod) {
        if (paymentRepository.existsByOrder_Id(order.getId())) {
            throw new BadRequestException("Payment already processed for order id: " + order.getId());
        }

        Payment payment = createPayment(order, paymentMethod);
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentDTO processSuccessfulPayment(Order order, PaymentMethod paymentMethod, String transactionId) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getFinalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(transactionId);
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentDTO createPendingPayment(Order order, PaymentMethod paymentMethod) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getFinalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public void updatePaymentToSuccess(Long orderId, String transactionId) {
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new DataNotFoundException("Payment not found for order id: " + orderId));

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(transactionId);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentToFailed(Long orderId, String failureReason) {
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new DataNotFoundException("Payment not found for order id: " + orderId));

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }

    @Override
    public void refundPaymentForOrder(Long orderId) {
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new DataNotFoundException("Payment not found for order id: " + orderId));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException("Only successful payments can be refunded. Current status: " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setTransactionId(payment.getTransactionId() + "_REFUNDED_" + System.currentTimeMillis());
        paymentRepository.save(payment);
    }

    @Override
    public Page<PaymentDTO> getAllPayments(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("paidAt").descending());
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(paymentMapper::toDto);
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentDTO getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new DataNotFoundException("Payment not found for order id: " + orderId));
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new DataNotFoundException("Payment not found with transaction id: " + transactionId));
        return paymentMapper.toDto(payment);
    }

    @Override
    public Page<PaymentDTO> getPaymentsByStatus(String status, int pageNo, int pageSize) {
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid payment status: " + status);
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("paidAt").descending());
        Page<Payment> payments = paymentRepository.findByStatus(paymentStatus, pageable);
        return payments.map(paymentMapper::toDto);
    }

    @Override
    public PaymentDTO refundPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Payment not found with id: " + id));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException("Only successful payments can be refunded. Current status: " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setTransactionId(payment.getTransactionId() + "_REFUNDED_" + System.currentTimeMillis());

        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(updatedPayment);
    }

    @Override
    public PaymentDTO updatePaymentStatus(Long id, UpdatePaymentStatusRequestDTO statusRequest) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Payment not found with id: " + id));

        PaymentStatus newStatus = statusRequest.getStatus();
        payment.setStatus(newStatus);

        if (newStatus == PaymentStatus.SUCCESS && payment.getPaidAt() == null) {
            payment.setPaidAt(LocalDateTime.now());
        }

        if (statusRequest.getTransactionId() != null && !statusRequest.getTransactionId().isEmpty()) {
            payment.setTransactionId(statusRequest.getTransactionId());
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(updatedPayment);
    }

    @Override
    public boolean hasSuccessfulPayment(Long orderId) {
        return paymentRepository.findByOrder_IdAndStatus(orderId, PaymentStatus.SUCCESS).isPresent();
    }

    @Override
    public boolean isPaymentPending(Long orderId) {
        return paymentRepository.findByOrder_IdAndStatus(orderId, PaymentStatus.PENDING).isPresent();
    }

    private Payment createPayment(Order order, PaymentRequestDTO request) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);

        if (!request.getPaymentMethod().equals(PaymentMethod.CASH_ON_DELIVERY)) {
            String transactionId = request.getTransactionId();
            if (transactionId == null || transactionId.isEmpty()) {
                transactionId = appConfig.generateTransactionId(
                        order.getUser().getId(), order.getId(), request.getPaymentMethod().name()
                );
            }
            payment.setTransactionId(transactionId);
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
        }

        return payment;
    }

    private Payment createPayment(Order order, PaymentMethod paymentMethod) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getFinalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.PENDING);

        if (!paymentMethod.equals(PaymentMethod.CASH_ON_DELIVERY)) {
            String transactionId = appConfig.generateTransactionId(
                    order.getUser().getId(), order.getId(), paymentMethod.name()
            );
            payment.setTransactionId(transactionId);
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
        }

        return payment;
    }
}