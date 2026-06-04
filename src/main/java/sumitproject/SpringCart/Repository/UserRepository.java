package sumitproject.SpringCart.Repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAndIsActive(String email, boolean b);

    User findByIdAndIsActive(Long id, boolean b);

    boolean existsByEmailAndIdNotAndIsActive(String email, Long id, boolean b);

    User findByEmailAndIsActive(String email, boolean b);

    boolean existsByEmailAndIsActiveTrue(@NotBlank(message = "Username is required") String username);

     Optional<User> findByEmailAndIsActiveTrue(String username);

    User findByProviderIdAndEmailAndIsActiveTrue(String providerId, String email);

    User findByProviderIdAndIsActiveTrue(String providerId);
}
