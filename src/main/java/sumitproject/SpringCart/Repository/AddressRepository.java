package sumitproject.SpringCart.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sumitproject.SpringCart.Entity.Address;

import java.util.Optional;
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUser_IdAndIsActiveTrueAndIdNot(Long userId, Long addressId);

    Page<Address> findAllByIsActiveTrue(Pageable pageable);

    Page<Address> findByUser_IdAndIsActiveTrue(Long userId, Pageable pageable);

    Address findByIdAndUser_IsActive(Long id, boolean b);

    Optional<Address> findByUser_IdAndIsDefaultAndIsActive(Long userId, boolean b, boolean b1);
}