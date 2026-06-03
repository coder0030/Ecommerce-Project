package sumitproject.SpringCart.Repository;

import sumitproject.SpringCart.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String code);

    Optional<Coupon> findByCodeAndIsActiveTrue(String code);

    List<Coupon> findByIsActiveTrue();

    List<Coupon> findByExpiryDateAfterAndIsActiveTrue(LocalDate date);

    @Query("SELECT c FROM Coupon c WHERE c.isActive = true AND c.expiryDate > CURRENT_DATE AND c.usedCount < c.usageLimit")
    List<Coupon> findAllValidCoupons();

    @Query("SELECT c FROM Coupon c WHERE c.code = :code AND c.isActive = true AND c.expiryDate > CURRENT_DATE AND c.usedCount < c.usageLimit")
    Optional<Coupon> findValidCouponByCode(@Param("code") String code);

    boolean existsByCode(String code);
}