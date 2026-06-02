package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.CouponDTO;
import sumitproject.SpringCart.RequestDTO.CouponRequestDTO;
import sumitproject.SpringCart.Service.CouponService;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    @Override
    public CouponDTO createCoupon(CouponRequestDTO couponRequestDTO) {
        return null;
    }

    @Override
    public Page<CouponDTO> getAllCoupons(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public CouponDTO getCouponById(Long id) {
        return null;
    }

    @Override
    public CouponDTO getCouponByCode(String code) {
        return null;
    }

    @Override
    public boolean validateCoupon(String code, Double orderAmount) {
        return false;
    }

    @Override
    public Page<CouponDTO> getActiveCoupons(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public void deleteCouponById(Long id) {

    }

    @Override
    public CouponDTO updateCouponById(Long id, CouponRequestDTO couponRequestDTO) {
        return null;
    }

    @Override
    public CouponDTO partialUpdateCouponById(Long id, CouponRequestDTO couponRequestDTO) {
        return null;
    }
}
