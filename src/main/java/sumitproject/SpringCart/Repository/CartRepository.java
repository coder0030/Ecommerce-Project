package sumitproject.SpringCart.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
    boolean existsByUser_IdAndIsActiveTrue(Long id);

    Cart findByIdAndIsActiveTrue(Long id);

    Cart findByUser_IdAndIsActiveTrue(Long userId);

    Cart findByUser_IdAndIsActiveFalse(Long userId);

    boolean existsByCartItems_IdAndUserId(Long cartItemId, Long principalId);

    boolean existsByIdAndUserId(Long cartId, Long principalId);

    boolean existsByIdAndUser_IdAndUser_IsActiveTrue(Long cartId, Long id);
}
