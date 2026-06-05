package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Coupons", description = "Manage discount coupons and promotional codes")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "Create coupon", description = "Creates a new discount coupon (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Coupon created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody CouponRequestDTO request) {
        CouponDTO coupon = couponService.createCoupon(request);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @Operation(summary = "Get coupon by ID", description = "Retrieves a specific coupon by its ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Long id) {
        CouponDTO coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    @Operation(summary = "Get coupon by code", description = "Retrieves a coupon by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CouponDTO> getCouponByCode(@PathVariable String code) {
        CouponDTO coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }

    @Operation(summary = "Get active coupons", description = "Retrieves all active coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupons retrieved successfully")
    })
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<List<CouponDTO>> getAllActiveCoupons() {
        List<CouponDTO> coupons = couponService.getAllActiveCoupons();
        return ResponseEntity.ok(coupons);
    }

    @Operation(summary = "Get valid coupons", description = "Retrieves all valid (not expired) coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupons retrieved successfully")
    })
    @GetMapping("/valid")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<List<CouponDTO>> getAllValidCoupons() {
        List<CouponDTO> coupons = couponService.getAllValidCoupons();
        return ResponseEntity.ok(coupons);
    }

    @Operation(summary = "Update coupon", description = "Updates an existing coupon (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponRequestDTO request) {
        CouponDTO coupon = couponService.updateCoupon(id, request);
        return ResponseEntity.ok(coupon);
    }

    @Operation(summary = "Delete coupon", description = "Permanently deletes a coupon (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Coupon deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deactivate coupon", description = "Deactivates a coupon without deleting it (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Coupon deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateCoupon(@PathVariable Long id) {
        couponService.deactivateCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Apply coupon", description = "Applies a coupon code to an order amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon applied successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid coupon or amount"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<Double> applyCoupon(@RequestParam String code, @RequestParam Double orderAmount) {
        Double discountedAmount = couponService.applyCoupon(code, orderAmount);
        return ResponseEntity.ok(discountedAmount);
    }

    @Operation(summary = "Validate coupon", description = "Checks if a coupon code is valid for an order amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validation completed successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @GetMapping("/validate")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<Boolean> validateCoupon(@RequestParam String code, @RequestParam Double orderAmount) {
        Boolean isValid = couponService.validateCoupon(code, orderAmount);
        return ResponseEntity.ok(isValid);
    }

    @Operation(summary = "Redeem coupon", description = "Redeems a coupon code for use")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Coupon redeemed successfully"),
            @ApiResponse(responseCode = "400", description = "Coupon cannot be redeemed"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @PostMapping("/{code}/redeem")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<Void> redeemCoupon(@PathVariable String code) {
        couponService.redeemCoupon(code);
        return ResponseEntity.noContent().build();
    }
}