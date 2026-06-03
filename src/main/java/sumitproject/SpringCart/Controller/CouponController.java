package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody CouponRequestDTO request) {
        CouponDTO coupon = couponService.createCoupon(request);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Long id) {
        CouponDTO coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponDTO> getCouponByCode(@PathVariable String code) {
        CouponDTO coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CouponDTO>> getAllActiveCoupons() {
        List<CouponDTO> coupons = couponService.getAllActiveCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/valid")
    public ResponseEntity<List<CouponDTO>> getAllValidCoupons() {
        List<CouponDTO> coupons = couponService.getAllValidCoupons();
        return ResponseEntity.ok(coupons);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponRequestDTO request) {
        CouponDTO coupon = couponService.updateCoupon(id, request);
        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCoupon(@PathVariable Long id) {
        couponService.deactivateCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/apply")
    public ResponseEntity<Double> applyCoupon(@RequestParam String code, @RequestParam Double orderAmount) {
        Double discountedAmount = couponService.applyCoupon(code, orderAmount);
        return ResponseEntity.ok(discountedAmount);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateCoupon(@RequestParam String code, @RequestParam Double orderAmount) {
        Boolean isValid = couponService.validateCoupon(code, orderAmount);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/{code}/redeem")
    public ResponseEntity<Void> redeemCoupon(@PathVariable String code) {
        couponService.redeemCoupon(code);
        return ResponseEntity.noContent().build();
    }
}