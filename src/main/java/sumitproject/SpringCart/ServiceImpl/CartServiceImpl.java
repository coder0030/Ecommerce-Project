package sumitproject.SpringCart.ServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.CartDTO;
import sumitproject.SpringCart.DTO.CartItemDTO;
import sumitproject.SpringCart.Entity.Cart;
import sumitproject.SpringCart.Entity.CartItem;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Mapper.CartItemMapper;
import sumitproject.SpringCart.Mapper.CartMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DataNotFoundException;
import sumitproject.SpringCart.Repository.CartItemRepository;
import sumitproject.SpringCart.Repository.CartRepository;
import sumitproject.SpringCart.RequestDTO.CartRequestDTO;
import sumitproject.SpringCart.Service.CartService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final AllRepositoryMethods allRepositoryMethods;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private List<CartItemDTO> convertCartItemsToDTOs(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return new ArrayList<>();
        }
        return cartItems.stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartDTO createCart(CartRequestDTO request) {
        User user = allRepositoryMethods.getUserById(request.getUserId());

        if (cartRepository.existsByUser_IdAndIsActiveTrue(user.getId())) {
            throw new BadRequestException(
                    "User already has an active cart. Use GET endpoint to fetch it."
            );
        }

        Cart newCart = new Cart();

        newCart.setUser(user);
        newCart.setTotalPrice(0.0);
        newCart.setUpdatedAt(LocalDateTime.now());
        newCart.setIsActive(true);
        newCart.setCartItems(new ArrayList<>());

        Cart savedCart = cartRepository.save(newCart);
        return cartMapper.toDto(savedCart, user, new ArrayList<>());
    }

    @Override
    public CartDTO getCartById(Long id) {
        Cart cart = allRepositoryMethods.getCartById(id);
        List<CartItemDTO> itemDTOs = convertCartItemsToDTOs(cart.getCartItems());
        return cartMapper.toDto(cart, cart.getUser(), itemDTOs);
    }

    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUser_IdAndIsActiveTrue(userId);
        if (cart == null) {
            throw new DataNotFoundException("No active cart found for user id: " + userId);
        }
        List<CartItemDTO> itemDTOs = convertCartItemsToDTOs(cart.getCartItems());
        return cartMapper.toDto(cart, cart.getUser(), itemDTOs);
    }

    @Override
    public CartDTO getCartWithItems(Long cartId) {
        Cart cart = allRepositoryMethods.getCartById(cartId);
        List<CartItemDTO> itemDTOs = convertCartItemsToDTOs(cart.getCartItems());
        return cartMapper.toDto(cart, cart.getUser(), itemDTOs);
    }

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = allRepositoryMethods.getCartById(cartId);

        if (!cart.getIsActive()) {
            throw new BadRequestException("Cannot clear an inactive cart");
        }

        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
        }

        cart.setTotalPrice(0.0);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public double getCartTotal(Long cartId) {
        Cart cart = allRepositoryMethods.getCartById(cartId);
        return cart.getTotalPrice() != null ? cart.getTotalPrice() : 0.0;
    }

    @Override
    @Transactional
    public CartDTO reactivateCart(Long userId) {
        Cart activeCart = cartRepository.findByUser_IdAndIsActiveTrue(userId);
        if (activeCart != null) {
            List<CartItemDTO> itemDTOs = convertCartItemsToDTOs(activeCart.getCartItems());
            return cartMapper.toDto(activeCart, activeCart.getUser(), itemDTOs);
        }

        Cart inactiveCart = cartRepository.findByUser_IdAndIsActiveFalse(userId);
        if (inactiveCart != null) {
            if (inactiveCart.getCartItems() != null && !inactiveCart.getCartItems().isEmpty()) {
                cartItemRepository.deleteAll(inactiveCart.getCartItems());
                inactiveCart.getCartItems().clear();
            }
            inactiveCart.setTotalPrice(0.0);
            inactiveCart.setIsActive(true);
            inactiveCart.setUpdatedAt(LocalDateTime.now());
            Cart reactivatedCart = cartRepository.save(inactiveCart);
            return cartMapper.toDto(reactivatedCart, reactivatedCart.getUser(), new ArrayList<>());
        }

        User user = allRepositoryMethods.getUserById(userId);
        Cart newCart = new Cart();

        newCart.setUser(user);
        newCart.setTotalPrice(0.0);
        newCart.setUpdatedAt(LocalDateTime.now());
        newCart.setIsActive(true);
        newCart.setCartItems(new ArrayList<>());

        Cart savedCart = cartRepository.save(newCart);
        return cartMapper.toDto(savedCart, user, new ArrayList<>());
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        Cart cart = allRepositoryMethods.getCartById(cartId);

        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
        }

        cartRepository.delete(cart);
    }
}