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
import sumitproject.SpringCart.RequestDTO.AddressUpdateRequestDTO;
import sumitproject.SpringCart.RequestDTO.OrderRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateOrderStatusRequestDTO;
import sumitproject.SpringCart.Service.OrderService;
import sumitproject.SpringCart.Service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    private final PaymentService paymentService;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, OrderRequestDTO request) {
        User user = allRepositoryMethods.getUserById(userId);
        Cart cart = getActiveCart(userId);
        Address address = allRepositoryMethods.getAddressById(request.getAddressId());

        double totalAmount = cart.getTotalPrice();
        Coupon coupon = resolveCoupon(request.getCouponCode(), totalAmount);
        double discountAmount = calculateDiscount(coupon, totalAmount);
        double finalAmount = totalAmount - discountAmount;

        Order savedOrder = buildAndSaveOrder(user, address, coupon, totalAmount, discountAmount, finalAmount);
        attachOrderItems(savedOrder, cart.getCartItems());
        reduceProductStock(cart.getCartItems());
        processPayment(savedOrder, request.getPaymentMethod());
        deactivateCart(cart);

        Payment payment = convertToPaymentEntity(paymentService.getPaymentByOrderId(savedOrder.getId()));
        return orderServiceMapper.toDto(savedOrder, payment);
    }

    @Override
    public Page<OrderDTO> getAllOrders(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        return orderServiceRepository.findAll(pageable)
                .map(this::mapOrderWithPayment);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = findOrderById(id);
        return mapOrderWithPayment(order);
    }

    @Override
    public Page<OrderDTO> getOrdersByUserId(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        return orderServiceRepository.findByUser_Id(userId, pageable)
                .map(this::mapOrderWithPayment);
    }

    @Override
    public Page<OrderDTO> getOrdersByStatus(String status, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return orderServiceRepository.findByStatus(orderStatus, pageable)
                .map(this::mapOrderWithPayment);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long id, UpdateOrderStatusRequestDTO statusRequest) {
        Order order = findOrderById(id);
        OrderStatus newStatus = statusRequest.getStatus();
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        if (newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.REFUNDED) {
            restoreStock(order.getOrderItems());
            refundIfPaid(id);
        }

        return mapOrderWithPayment(orderServiceRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(Long id, String reason) {
        Order order = findOrderById(id);
        validateCancellable(order);

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        restoreStock(order.getOrderItems());
        refundIfPaid(id);

        return mapOrderWithPayment(orderServiceRepository.save(order));
    }

    @Override
    @Transactional
    public void deleteOrderById(Long id) {
        Order order = findOrderById(id);

        boolean isDeletable = order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.REFUNDED;

        if (!isDeletable) {
            throw new BadRequestException("Only cancelled or refunded orders can be deleted");
        }

        orderServiceRepository.delete(order);
    }

    @Override
    @Transactional
    public void deleteAddressById(Long userId, AddressUpdateRequestDTO request) {
        Address address = allRepositoryMethods.getAddressById(request.getAddressId());
        Address newAddress = allRepositoryMethods.getAddressById(request.getNewAddressId());
        Order order = findActiveOrder(request);

        validateAddressHasActiveOrder(address.getId());

        order.setAddress(newAddress);

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            address.setIsDefault(false);
            newAddress.setIsDefault(true);
        }

        address.setActive(false);
        addressRepository.saveAll(List.of(address, newAddress));
        orderServiceRepository.save(order);
    }

    @Override
    @Transactional
    public void updateAddress(Long userId, AddressUpdateRequestDTO request) {
        Address newAddress = allRepositoryMethods.getAddressById(request.getNewAddressId());
        Order order = findActiveOrder(request);

        validateAddressHasActiveOrder(request.getAddressId());

        order.setAddress(newAddress);
        addressRepository.save(newAddress);
        orderServiceRepository.save(order);
    }

    private Cart getActiveCart(Long userId) {
        Cart cart = cartRepository.findByUser_IdAndIsActiveTrue(userId);
        if (cart == null) {
            throw new DataNotFoundException("No active cart found for user");
        }
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        return cart;
    }

    private Coupon resolveCoupon(String couponCode, double totalAmount) {
        if (couponCode == null || couponCode.isBlank()) return null;

        Coupon coupon = couponRepository.findValidCouponByCode(couponCode.toUpperCase())
                .orElseThrow(() -> new BadRequestException("Invalid or expired coupon: " + couponCode));

        if (!coupon.canApply(totalAmount)) {
            throw new BadRequestException("Minimum order amount required: " + coupon.getMinOrderAmount());
        }

        coupon.incrementUsedCount();
        couponRepository.save(coupon);
        return coupon;
    }

    private double calculateDiscount(Coupon coupon, double totalAmount) {
        if (coupon == null) return 0.0;
        return totalAmount - coupon.applyDiscount(totalAmount);
    }

    private Order buildAndSaveOrder(User user, Address address, Coupon coupon,
                                    double totalAmount, double discountAmount, double finalAmount) {
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setCoupon(coupon);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(finalAmount);
        order.setStatus(OrderStatus.PENDING);
        return orderServiceRepository.save(order);
    }

    private void attachOrderItems(Order order, List<CartItem> cartItems) {
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(cartItem.getProduct());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPriceAtPurchase(cartItem.getPrice());
                    return item;
                })
                .toList();

        order.setOrderItems(orderItems);
        orderServiceRepository.save(order);
    }

    private void reduceProductStock(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product product = allRepositoryMethods.getProductById(cartItem.getProduct().getId());
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock for: " + product.getName()
                        + ". Available: " + product.getStock());
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
    }

    private void processPayment(Order order, PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.CASH_ON_DELIVERY) {
            paymentService.createPendingPayment(order, paymentMethod);
        } else {
            paymentService.processPaymentForOrder(order, paymentMethod);
        }
    }

    private void deactivateCart(Cart cart) {
        cart.setIsActive(false);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    private void restoreStock(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
    }

    private void refundIfPaid(Long orderId) {
        if (paymentService.hasSuccessfulPayment(orderId)) {
            paymentService.refundPaymentForOrder(orderId);
        }
    }

    private void validateCancellable(Order order) {
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot cancel a delivered order");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order is already cancelled");
        }
    }

    private void validateAddressHasActiveOrder(Long addressId) {
        boolean hasActiveOrder = orderServiceRepository.existsByAddress_IdAndAddress_IsActiveTrueAndStatusIn(
                addressId, Set.of(OrderStatus.PENDING, OrderStatus.CONFIRMED));

        if (!hasActiveOrder) {
            throw new BadRequestException("Cannot modify address. No active order found for this address.");
        }
    }

    private Order findOrderById(Long id) {
        return orderServiceRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + id));
    }

    private Order findActiveOrder(AddressUpdateRequestDTO request) {
        return orderServiceRepository.findByIdAndStatusIn(
                request.getOrderId(), Set.of(OrderStatus.PENDING, OrderStatus.CONFIRMED));
    }

    private OrderDTO mapOrderWithPayment(Order order) {
        Payment payment = convertToPaymentEntity(paymentService.getPaymentByOrderId(order.getId()));
        return orderServiceMapper.toDto(order, payment);
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