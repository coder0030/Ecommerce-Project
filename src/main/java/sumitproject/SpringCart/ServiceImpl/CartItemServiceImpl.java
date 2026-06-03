package sumitproject.SpringCart.ServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.DTO.CartItemDTO;
import sumitproject.SpringCart.Entity.Cart;
import sumitproject.SpringCart.Entity.CartItem;
import sumitproject.SpringCart.Entity.Product;
import sumitproject.SpringCart.Mapper.CartItemMapper;
import sumitproject.SpringCart.Mapper.CartMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DataNotFoundException;
import sumitproject.SpringCart.Repository.CartItemRepository;
import sumitproject.SpringCart.Repository.CartRepository;
import sumitproject.SpringCart.RequestDTO.CartItemRequestDTO;
import sumitproject.SpringCart.RequestDTO.UpdateCartItemRequestDTO;
import sumitproject.SpringCart.Service.CartItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final AllRepositoryMethods allRepositoryMethods;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;

    private void updateCartTotal(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(total);
    }

    @Override
    public CartItemDTO getCartItemById(Long id) {
        CartItem cartItem = allRepositoryMethods.getCartItemById(id);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    @Transactional
    public CartItemDTO addItemToCart(Long cartId, CartItemRequestDTO cartItemRequestDTO) {
        Cart cart = allRepositoryMethods.getCartById(cartId);

        if (!cart.getIsActive()) {
            throw new BadRequestException("Cannot add items to an inactive cart");
        }

        if (cartItemRequestDTO.getQuantity() == null || cartItemRequestDTO.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        Product product = allRepositoryMethods.getProductById(cartItemRequestDTO.getProductId());

        if (product.getStock() < 1) {
            throw new BadRequestException("Product '" + product.getName() + "' is out of stock");
        }

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + cartItemRequestDTO.getQuantity();

            if (product.getStock() < newQuantity) {
                throw new BadRequestException(
                        "Insufficient stock. Available: " + product.getStock() +
                                ", Already in cart: " + existingCartItem.getQuantity() +
                                ", Requested more: " + cartItemRequestDTO.getQuantity()
                );
            }
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.setPrice(product.getPrice());
            cartItemRepository.save(existingCartItem);

            updateCartTotal(cart);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);

            return cartItemMapper.toDto(existingCartItem);

        } else {
            if (product.getStock() < cartItemRequestDTO.getQuantity()) {
                throw new BadRequestException(
                        "Product '" + product.getName() +
                                "' has only " + product.getStock() + " items available"
                );
            }

            CartItem cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequestDTO.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setAddedAt(LocalDateTime.now());;

            cart.addCartItem(cartItem);
            CartItem savedCartItem = cartItemRepository.save(cartItem);

            updateCartTotal(cart);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);

            return cartItemMapper.toDto(savedCartItem);
        }
    }

    @Override
    @Transactional
    public CartItemDTO updateCartItem(UpdateCartItemRequestDTO updateRequest) {
        CartItem cartItem = allRepositoryMethods.getCartItemById(updateRequest.getCartItemId());
        Cart cart = cartItem.getCart();

        if (!cart.getIsActive()) {
            throw new BadRequestException("Cannot update items in an inactive cart");
        }

        if (updateRequest.getQuantity() == null || updateRequest.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        Product product = cartItem.getProduct();
        if (product.getStock() < updateRequest.getQuantity()) {
            throw new BadRequestException(
                    "Insufficient stock. Available: " + product.getStock()
            );
        }

        cartItem.setQuantity(updateRequest.getQuantity());
        cartItem.setPrice(product.getPrice());
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        updateCartTotal(cart);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        return cartItemMapper.toDto(updatedCartItem);
    }

    @Override
    @Transactional
    public CartItemDTO updateCartItemQuantity(Long cartItemId, Integer quantity) {
        UpdateCartItemRequestDTO request = UpdateCartItemRequestDTO.builder()
                .cartItemId(cartItemId)
                .quantity(quantity)
                .build();
        return updateCartItem(request);
    }

    @Override
    @Transactional
    public void deleteCartItemById(Long id) {
        CartItem cartItem = allRepositoryMethods.getCartItemById(id);
        Cart cart = cartItem.getCart();

        if (!cart.getIsActive()) {
            throw new BadRequestException("Cannot remove items from an inactive cart");
        }

        cart.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);

        updateCartTotal(cart);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public Page<CartItemDTO> getCartItemsByCartId(Long cartId, int pageNo, int pageSize) {
        Sort sort = Sort.by("addedAt").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<CartItem> cartItemPage = cartItemRepository.findByCart_IdAndCart_IsActiveTrue(
                cartId, pageable);

        if (cartItemPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return cartItemPage.map(cartItemMapper::toDto);
    }

    @Override
    public CartItemDTO getCartItemByCartAndProduct(Long cartId, Long productId) {
        CartItem cartItem = cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId);
        if (cartItem == null) {
            throw new DataNotFoundException("CartItem not exists.");
        }
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    @Transactional
    public List<CartItemDTO> addItemsToCartBulk(Long cartId, List<CartItemRequestDTO> cartItemRequestDTOs) {
        List<CartItemDTO> addedItems = new ArrayList<>();

        for (int i = 0; i < cartItemRequestDTOs.size(); i++) {
            CartItemRequestDTO request = cartItemRequestDTOs.get(i);
            try {
                CartItemDTO addedItem = addItemToCart(cartId, request);
                addedItems.add(addedItem);
            } catch (BadRequestException e) {
                throw new BadRequestException("Item " + (i + 1) + " failed: " + e.getMessage());
            }
        }

        return addedItems;
    }

    @Override
    @Transactional
    public void deleteAllCartItemsByCartId(Long cartId) {
        Cart cart = allRepositoryMethods.getCartById(cartId);

        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
            cart.setTotalPrice(0.0);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }
    }

    @Override
    public Integer getCartItemCountByCartId(Long cartId) {
        return cartItemRepository.countByCartId(cartId);
    }

    @Override
    public Double getCartItemSubtotal(Long id) {
        CartItem cartItem = allRepositoryMethods.getCartItemById(id);
        return cartItem.getPrice() * cartItem.getQuantity();
    }

    @Override
    public Boolean isProductInCart(Long cartId, Long productId) {
        return cartItemRepository.existsByCartIdAndProductId(cartId, productId);
    }
}