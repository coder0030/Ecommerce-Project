package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.CouponDTO;
import sumitproject.SpringCart.RequestDTO.CouponRequestDTO;
import sumitproject.SpringCart.Service.CouponService;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/create")
    public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody CouponRequestDTO couponRequestDTO) {
        CouponDTO createdCoupon = couponService.createCoupon(couponRequestDTO);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<CouponDTO>> getAllCoupons(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<CouponDTO> coupons = couponService.getAllCoupons(pageNo, pageSize);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Long id) {
        CouponDTO couponDTO = couponService.getCouponById(id);
        return new ResponseEntity<>(couponDTO, HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponDTO> getCouponByCode(@PathVariable String code) {
        CouponDTO couponDTO = couponService.getCouponByCode(code);
        return new ResponseEntity<>(couponDTO, HttpStatus.OK);
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<Boolean> validateCoupon(@PathVariable String code,
                                                  @RequestParam Double orderAmount) {
        boolean isValid = couponService.validateCoupon(code, orderAmount);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<CouponDTO>> getActiveCoupons(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<CouponDTO> coupons = couponService.getActiveCoupons(pageNo, pageSize);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCouponById(@PathVariable Long id) {
        couponService.deleteCouponById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CouponDTO> updateCouponById(@PathVariable Long id,
                                                      @Valid @RequestBody CouponRequestDTO couponRequestDTO) {
        CouponDTO updatedCoupon = couponService.updateCouponById(id, couponRequestDTO);
        return new ResponseEntity<>(updatedCoupon, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CouponDTO> partialUpdateCouponById(@PathVariable Long id,
                                                             @RequestBody CouponRequestDTO couponRequestDTO) {
        CouponDTO updatedCoupon = couponService.partialUpdateCouponById(id, couponRequestDTO);
        return new ResponseEntity<>(updatedCoupon, HttpStatus.OK);
    }
}
