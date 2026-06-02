package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CouponDTO;
import sumitproject.SpringCart.Entity.Coupon;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.RequestDTO.CouponRequestDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CouponMapper {

    public CouponDTO toDto(Coupon coupon) {
        if (coupon == null) return null;

        CouponDTO.CouponDTOBuilder builder = CouponDTO.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountPercent(coupon.getDiscountPercent())
                .minOrderAmount(coupon.getMinOrderAmount())
                .expiryDate(coupon.getExpiryDate())
                .usageLimit(coupon.getUsageLimit())
                .usedCount(coupon.getUsedCount())
                .isActive(coupon.getIsActive())
                .createdAt(coupon.getCreatedAt());

        // Calculate if coupon is valid
        boolean isValid = coupon.getIsActive() != null && coupon.getIsActive() &&
                coupon.getExpiryDate() != null &&
                !coupon.getExpiryDate().isBefore(LocalDate.now()) &&
                (coupon.getUsageLimit() == null ||
                        coupon.getUsedCount() == null ||
                        coupon.getUsedCount() < coupon.getUsageLimit());
        builder.isValid(isValid);

        return builder.build();
    }

    public Coupon toEntity(CouponRequestDTO couponDTO, Coupon coupon) {
        if (couponDTO == null) return null;

        if (couponDTO.getCode() != null) {
            coupon.setCode(couponDTO.getCode());
        }

        if (couponDTO.getExpiryDate() != null) {
            coupon.setExpiryDate(couponDTO.getExpiryDate());
        }
        if (couponDTO.getUsageLimit() != null) {
            coupon.setUsageLimit(couponDTO.getUsageLimit());
        }

        coupon.setDiscountPercent(couponDTO.getDiscountPercent());
        coupon.setMinOrderAmount(couponDTO.getMinOrderAmount());

        return coupon;
    }

    public List<CouponDTO> toDtoList(List<Coupon> coupons) {
        if (coupons == null || coupons.isEmpty()) return null;
        return coupons.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Coupon updateToEntity(CouponRequestDTO couponDTO, Coupon coupon) {
        boolean nullValue = false;

        if (couponDTO.getCode() == null) nullValue = true;
        if (couponDTO.getExpiryDate() == null) nullValue = true;
        if (couponDTO.getUsageLimit() == null) nullValue = true;

        if (nullValue) {
            throw new BadRequestException("Incomplete data provided.");
        }

        coupon.setCode(couponDTO.getCode());
        coupon.setDiscountPercent(couponDTO.getDiscountPercent());
        coupon.setMinOrderAmount(couponDTO.getMinOrderAmount());
        coupon.setExpiryDate(couponDTO.getExpiryDate());
        coupon.setUsageLimit(couponDTO.getUsageLimit());

        return coupon;
    }
}
