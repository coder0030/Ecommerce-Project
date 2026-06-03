package sumitproject.SpringCart.ServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.OrderItemDTO;
import sumitproject.SpringCart.Entity.OrderItem;
import sumitproject.SpringCart.Helper.OrderStatus;
import sumitproject.SpringCart.Mapper.OrderItemMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DataNotFoundException;
import sumitproject.SpringCart.Repository.OrderItemRepository;
import sumitproject.SpringCart.Repository.OrderServiceRepository;
import sumitproject.SpringCart.Service.OrderItemService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final AllRepositoryMethods allRepositoryMethods;
    private final OrderServiceRepository orderServiceRepository;

    @Override
    public OrderItemDTO getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "OrderItem not found with id: " + id
                ));
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public List<OrderItemDTO> getItemsByOrderId(Long orderId) {
        if(!orderServiceRepository.existsById(orderId)) {
            throw new DataNotFoundException("Order id: " + orderId + " not exists.");
        }

        List<OrderItem> items = orderItemRepository.findByOrder_Id(orderId);
        if (items.isEmpty()) return Collections.emptyList();

        return orderItemMapper.toDtoList(items);
    }

    @Override
    public Page<OrderItemDTO> getItemsByOrderIdPaginated(Long orderId, int pageNo, int pageSize) {
        if(!orderServiceRepository.existsById(orderId)) {
            throw new DataNotFoundException("Order id: " + orderId + " not exists.");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize,
                Sort.by("id").ascending());

        Page<OrderItem> itemPage = orderItemRepository.findByOrder_Id(orderId, pageable);
        if (itemPage.isEmpty()) return Page.empty(pageable);

        return itemPage.map(orderItemMapper::toDto);
    }

    @Override
    public Double calculateOrderTotal(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrder_Id(orderId);
        if (items.isEmpty()) return 0.0;

        return items.stream()
                .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
                .sum();
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new DataNotFoundException(
                        "OrderItem not found with id: " + orderItemId
                ));

        if (orderItem.getOrder().getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException(
                    "Cannot delete item from a " +
                            orderItem.getOrder().getStatus() + " order"
            );
        }

        orderItemRepository.delete(orderItem);
    }
}