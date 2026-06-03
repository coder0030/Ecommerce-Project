package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumitproject.SpringCart.DTO.CouponDTO;
import sumitproject.SpringCart.Entity.Coupon;
import sumitproject.SpringCart.Mapper.CouponMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DataNotFoundException;
import sumitproject.SpringCart.Repository.CouponRepository;
import sumitproject.SpringCart.RequestDTO.CouponRequestDTO;
import sumitproject.SpringCart.Service.CouponService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    private Coupon findCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Coupon not found with id: " + id));
    }

    private void validateDiscountPercent(Double discountPercent) {
        if (discountPercent == null || discountPercent <= 0 || discountPercent > 100) {
            throw new BadRequestException("Discount percent must be between 1 and 100");
        }
    }

    private void validateExpiryDate(LocalDate expiryDate) {
        if (expiryDate == null) {
            throw new BadRequestException("Expiry date is required");
        }
        if (expiryDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Expiry date cannot be in the past");
        }
    }

    private void validateCodeUniqueness(String code, Long excludeId) {
        if (couponRepository.existsByCode(code.toUpperCase())) {
            if (excludeId == null) {
                throw new BadRequestException("Coupon code already exists: " + code);
            }
            Coupon existingCoupon = couponRepository.findByCode(code.toUpperCase()).get();
            if (!existingCoupon.getId().equals(excludeId)) {
                throw new BadRequestException("Coupon code already exists: " + code);
            }
        }
    }

    @Override
    @Transactional
    public CouponDTO createCoupon(CouponRequestDTO request) {
        if (request.getCode() == null || request.getCode().isBlank()) {
            throw new BadRequestException("Coupon code is required");
        }

        validateCodeUniqueness(request.getCode(), null);
        validateDiscountPercent(request.getDiscountPercent());
        validateExpiryDate(request.getExpiryDate());

        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode().toUpperCase());
        coupon.setDiscountPercent(request.getDiscountPercent());
        coupon.setMinOrderAmount(request.getMinOrderAmount() != null ? request.getMinOrderAmount() : 0.0);
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit() != null ? request.getUsageLimit() : 1);
        coupon.setUsedCount(0);
        coupon.setIsActive(true);

        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    public CouponDTO getCouponById(Long id) {
        return couponMapper.toDto(findCouponById(id));
    }

    @Override
    public CouponDTO getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new DataNotFoundException("Coupon not found with code: " + code));
        return couponMapper.toDto(coupon);
    }

    @Override
    public List<CouponDTO> getAllActiveCoupons() {
        return couponRepository.findByIsActiveTrue()
                .stream()
                .map(couponMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponDTO> getAllValidCoupons() {
        return couponRepository.findAllValidCoupons()
                .stream()
                .map(couponMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponDTO updateCoupon(Long id, CouponRequestDTO request) {
        Coupon coupon = findCouponById(id);

        String newCode = request.getCode().toUpperCase();
        if (!coupon.getCode().equals(newCode)) {
            validateCodeUniqueness(newCode, id);
        }

        if (request.getDiscountPercent() != null) {
            validateDiscountPercent(request.getDiscountPercent());
            coupon.setDiscountPercent(request.getDiscountPercent());
        }

        if (request.getExpiryDate() != null) {
            validateExpiryDate(request.getExpiryDate());
            coupon.setExpiryDate(request.getExpiryDate());
        }

        if (request.getUsageLimit() != null) {
            if (request.getUsageLimit() < coupon.getUsedCount()) {
                throw new BadRequestException("Usage limit cannot be less than already used count: " + coupon.getUsedCount());
            }
            coupon.setUsageLimit(request.getUsageLimit());
        }

        coupon.setCode(newCode);
        coupon.setMinOrderAmount(request.getMinOrderAmount() != null ? request.getMinOrderAmount() : coupon.getMinOrderAmount());

        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    @Transactional
    public void deactivateCoupon(Long id) {
        Coupon coupon = findCouponById(id);
        if (!coupon.getIsActive()) {
            throw new BadRequestException("Coupon is already inactive");
        }
        coupon.setIsActive(false);
        couponRepository.save(coupon);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = findCouponById(id);
        if (coupon.getUsedCount() > 0) {
            coupon.setIsActive(false);
            couponRepository.save(coupon);
        } else {
            couponRepository.delete(coupon);
        }
    }

    @Override
    @Transactional
    public Double applyCoupon(String code, Double orderAmount) {
        Coupon coupon = couponRepository.findValidCouponByCode(code.toUpperCase())
                .orElseThrow(() -> new BadRequestException("Invalid or expired coupon code: " + code));

        if (orderAmount < coupon.getMinOrderAmount()) {
            throw new BadRequestException("Minimum order amount required to apply this coupon: " + coupon.getMinOrderAmount());
        }

        Double discount = orderAmount * (coupon.getDiscountPercent() / 100);
        Double discountedAmount = orderAmount - discount;

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
            coupon.setIsActive(false);
        }
        couponRepository.save(coupon);

        return discountedAmount;
    }

    @Override
    public boolean validateCoupon(String code, Double orderAmount) {
        return couponRepository.findValidCouponByCode(code.toUpperCase())
                .map(coupon -> orderAmount >= coupon.getMinOrderAmount())
                .orElse(false);
    }

    @Override
    @Transactional
    public void redeemCoupon(String code) {
        Coupon coupon = couponRepository.findValidCouponByCode(code.toUpperCase())
                .orElseThrow(() -> new BadRequestException("Invalid or expired coupon code: " + code));
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
            coupon.setIsActive(false);
        }
        couponRepository.save(coupon);
    }
}