package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.CouponDTO;
import sumitproject.SpringCart.RequestDTO.CouponRequestDTO;
import sumitproject.SpringCart.Service.CouponService;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody CouponRequestDTO request) {
        CouponDTO coupon = couponService.createCoupon(request);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Long id) {
        CouponDTO coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CouponDTO> getCouponByCode(@PathVariable String code) {
        CouponDTO coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<List<CouponDTO>> getAllActiveCoupons() {
        List<CouponDTO> coupons = couponService.getAllActiveCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/valid")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<List<CouponDTO>> getAllValidCoupons() {
        List<CouponDTO> coupons = couponService.getAllValidCoupons();
        return ResponseEntity.ok(coupons);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponRequestDTO request) {
        CouponDTO coupon = couponService.updateCoupon(id, request);
        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateCoupon(@PathVariable Long id) {
        couponService.deactivateCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<Double> applyCoupon(@RequestParam String code, @RequestParam Double orderAmount) {
        Double discountedAmount = couponService.applyCoupon(code, orderAmount);
        return ResponseEntity.ok(discountedAmount);
    }

    @GetMapping("/validate")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<Boolean> validateCoupon(@RequestParam String code, @RequestParam Double orderAmount) {
        Boolean isValid = couponService.validateCoupon(code, orderAmount);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/{code}/redeem")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<Void> redeemCoupon(@PathVariable String code) {
        couponService.redeemCoupon(code);
        return ResponseEntity.noContent().build();
    }
}