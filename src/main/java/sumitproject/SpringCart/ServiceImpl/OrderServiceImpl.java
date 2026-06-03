package sumitproject.SpringCart.ServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.OrderDTO;
import sumitproject.SpringCart.DTO.PaymentDTO;
import sumitproject.SpringCart.Entity.*;
import sumitproject.SpringCart.Helper.OrderStatus;
import sumitproject.SpringCart.Helper.PaymentMethod;
import sumitproject.SpringCart.Helper.PaymentStatus;
import sumitproject.SpringCart.Mapper.OrderServiceMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DataNotFoundException;
import sumitproject.SpringCart.Repository.*;
import sumitproject.SpringCart.RequestDTO.OrderRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateOrderStatusRequestDTO;
import sumitproject.SpringCart.Service.OrderService;
import sumitproject.SpringCart.Service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AllRepositoryMethods allRepositoryMethods;
    private final OrderServiceRepository orderServiceRepository;
    private final OrderServiceMapper orderServiceMapper;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final PaymentService paymentService; // Inject PaymentService

    private void reduceProductStock(List<CartItem> cartItemList) {
        for (CartItem cartItem : cartItemList) {
            Product product = allRepositoryMethods.getProductById(cartItem.getProduct().getId());
            int quantity = cartItem.getQuantity();

            if (product.getStock() < quantity) {
                throw new BadRequestException("Available stock for " + product.getName() + " is: " + product.getStock());
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, OrderRequestDTO request) {
        User user = allRepositoryMethods.getUserById(userId);

        Cart cart = cartRepository.findByUser_IdAndIsActiveTrue(userId);
        if (cart == null) {
            throw new DataNotFoundException("No active cart found for user");
        }
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Address address = allRepositoryMethods.getAddressById(request.getAddressId());

        Coupon coupon = null;
        double discountAmount = 0.0;
        double totalAmount = cart.getTotalPrice();

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            coupon = couponRepository.findValidCouponByCode(request.getCouponCode().toUpperCase())
                    .orElseThrow(() -> new BadRequestException("Invalid or expired coupon code: " + request.getCouponCode()));

            if (!coupon.canApply(totalAmount)) {
                throw new BadRequestException("Minimum order amount required: " + coupon.getMinOrderAmount());
            }

            double finalAfterDiscount = coupon.applyDiscount(totalAmount);
            discountAmount = totalAmount - finalAfterDiscount;

            coupon.incrementUsedCount();
            couponRepository.save(coupon);
        }

        double finalAmount = totalAmount - discountAmount;

        Order order = new Order();
        order.setUser(user);
        order.setCoupon(coupon);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(finalAmount);
        order.setStatus(OrderStatus.PENDING);

        order.setDeliveryName(address.getStreet());
        order.setDeliveryPhone(address.getCity());
        order.setDeliveryStreet(address.getStreet());
        order.setDeliveryCity(address.getCity());
        order.setDeliveryState(address.getState());
        order.setDeliveryPincode(address.getPincode());
        order.setDeliveryCountry(address.getCountry() != null ? address.getCountry() : "India");

        Order savedOrder = orderServiceRepository.save(order);

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPriceAtPurchase(cartItem.getPrice());
                    return orderItem;
                })
                .collect(Collectors.toList());

        savedOrder.setOrderItems(orderItems);
        orderServiceRepository.save(savedOrder);

        reduceProductStock(cart.getCartItems());

        if (request.getPaymentMethod().equals(PaymentMethod.CASH_ON_DELIVERY)) {
            paymentService.createPendingPayment(savedOrder, request.getPaymentMethod());
        } else {
            paymentService.processPaymentForOrder(savedOrder, request.getPaymentMethod());
        }

        cart.setIsActive(false);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        var payment = paymentService.getPaymentByOrderId(savedOrder.getId());
        return orderServiceMapper.toDto(savedOrder, convertToPaymentEntity(payment));
    }

    @Override
    public Page<OrderDTO> getAllOrders(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderServiceRepository.findAll(pageable);
        return orderPage.map(order -> {
            var payment = paymentService.getPaymentByOrderId(order.getId());
            return orderServiceMapper.toDto(order, convertToPaymentEntity(payment));
        });
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderServiceRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + id));
        var payment = paymentService.getPaymentByOrderId(id);
        return orderServiceMapper.toDto(order, convertToPaymentEntity(payment));
    }

    @Override
    public Page<OrderDTO> getOrdersByUserId(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderServiceRepository.findByOrderItems_Order_Id(userId, pageable);
        return orderPage.map(order -> {
            var payment = paymentService.getPaymentByOrderId(order.getId());
            return orderServiceMapper.toDto(order, convertToPaymentEntity(payment));
        });
    }

    @Override
    public Page<OrderDTO> getOrdersByStatus(String status, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        Page<Order> orderPage = orderServiceRepository.findByStatus(orderStatus, pageable);
        return orderPage.map(order -> {
            var payment = paymentService.getPaymentByOrderId(order.getId());
            return orderServiceMapper.toDto(order, convertToPaymentEntity(payment));
        });
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long id, UpdateOrderStatusRequestDTO statusRequest) {
        Order order = orderServiceRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + id));

        OrderStatus newStatus = statusRequest.getStatus();
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        if (newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.REFUNDED) {

            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                product.setStock(product.getStock() + orderItem.getQuantity());
                productRepository.save(product);
            }

            if (paymentService.hasSuccessfulPayment(id)) {
                paymentService.refundPaymentForOrder(id);
            }
        }

        Order updatedOrder = orderServiceRepository.save(order);
        var payment = paymentService.getPaymentByOrderId(id);
        return orderServiceMapper.toDto(updatedOrder, convertToPaymentEntity(payment));
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(Long id, String reason) {
        Order order = orderServiceRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot cancel delivered order");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        // Restore stock
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() + orderItem.getQuantity());
            productRepository.save(product);
        }

        Order cancelledOrder = orderServiceRepository.save(order);

        if (paymentService.hasSuccessfulPayment(id)) {
            paymentService.refundPaymentForOrder(id);
        }

        var payment = paymentService.getPaymentByOrderId(id);
        return orderServiceMapper.toDto(cancelledOrder, convertToPaymentEntity(payment));
    }

    @Override
    @Transactional
    public void deleteOrderById(Long id) {
        Order order = orderServiceRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + id));

        if (order.getStatus() != OrderStatus.CANCELLED && order.getStatus() != OrderStatus.REFUNDED) {
            throw new BadRequestException("Only cancelled or refunded orders can be deleted");
        }

        orderServiceRepository.delete(order);
    }

    private Payment convertToPaymentEntity(PaymentDTO paymentDTO) {
        if (paymentDTO == null) return null;
        return Payment.builder()
                .id(paymentDTO.getId())
                .amount(paymentDTO.getAmount())
                .paymentMethod(paymentDTO.getPaymentMethod())
                .status(paymentDTO.getStatus())
                .transactionId(paymentDTO.getTransactionId())
                .paidAt(paymentDTO.getPaidAt())
                .build();
    }
}