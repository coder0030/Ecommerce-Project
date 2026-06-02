package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CouponDTO;
import sumitproject.SpringCart.RequestDTO.CouponRequestDTO;

@Component
public interface CouponService {
    CouponDTO createCoupon(@Valid CouponRequestDTO couponRequestDTO);

    Page<CouponDTO> getAllCoupons(int pageNo, int pageSize);

    CouponDTO getCouponById(Long id);

    CouponDTO getCouponByCode(String code);

    boolean validateCoupon(String code, Double orderAmount);

    Page<CouponDTO> getActiveCoupons(int pageNo, int pageSize);

    void deleteCouponById(Long id);

    CouponDTO updateCouponById(Long id, @Valid CouponRequestDTO couponRequestDTO);

    CouponDTO partialUpdateCouponById(Long id, CouponRequestDTO couponRequestDTO);
}
