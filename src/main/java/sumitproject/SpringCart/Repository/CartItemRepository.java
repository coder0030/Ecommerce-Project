package sumitproject.SpringCart.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findByCart_IdAndCart_IsActiveTrue(Long cartId, Pageable pageable);

    CartItem findByCart_IdAndProduct_Id(Long cartId, Long productId);

    Integer countByCartId(Long cartId);

    Boolean existsByCartIdAndProductId(Long cartId, Long productId);

    boolean existsByIdAndCart_User_IdAndCart_User_IsActiveTrue(Long cartItemId, Long id);
}
