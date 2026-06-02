package sumitproject.SpringCart.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAndIsActive(String email, boolean b);

    User findByIdAndIsActive(Long id, boolean b);

    boolean existsByEmailAndIdNotAndIsActive(String email, Long id, boolean b);

    User findByEmailAndIsActive(String email, boolean b);
}
