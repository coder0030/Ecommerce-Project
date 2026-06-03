package sumitproject.SpringCart.Service;

import sumitproject.SpringCart.DTO.CouponDTO;
import sumitproject.SpringCart.RequestDTO.CouponRequestDTO;
import java.util.List;

public interface CouponService {

    CouponDTO createCoupon(CouponRequestDTO request);

    CouponDTO getCouponById(Long id);

    CouponDTO getCouponByCode(String code);

    List<CouponDTO> getAllActiveCoupons();

    List<CouponDTO> getAllValidCoupons();

    CouponDTO updateCoupon(Long id, CouponRequestDTO request);

    void deleteCoupon(Long id);

    void deactivateCoupon(Long id);

    Double applyCoupon(String code, Double orderAmount);

    boolean validateCoupon(String code, Double orderAmount);

    void redeemCoupon(String code);
}