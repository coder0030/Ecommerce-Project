package sumitproject.SpringCart.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Repository.CartItemRepository;
import sumitproject.SpringCart.Repository.CartRepository;
import sumitproject.SpringCart.Repository.OrderServiceRepository;
import sumitproject.SpringCart.Repository.ReviewRepository;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderServiceRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public boolean orderVerify(Long orderId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderRepository.existsByIdAndUser_IdAndUser_IsActiveTrue(orderId, user.getId());
    }

    public boolean reviewVerify(Long reviewId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return reviewRepository.existsByIdAndUser_IdAndUser_IsActiveTrue(reviewId, user.getId());
    }

    public boolean cartVerify(Long cartId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartRepository.existsByIdAndUser_IdAndUser_IsActiveTrue(cartId, user.getId());
    }

    public boolean verifyItem(Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartItemRepository.existsByIdAndCart_User_IdAndCart_User_IsActiveTrue(cartItemId, user.getId());
    }
}
