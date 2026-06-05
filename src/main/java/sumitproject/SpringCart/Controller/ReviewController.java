package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.ReviewDTO;
import sumitproject.SpringCart.RequestDTO.ReviewRequestDTO;
import sumitproject.SpringCart.Service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Manage product reviews and ratings")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Create review", description = "Creates a new product review (Customer only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User or product not found")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
        ReviewDTO review = reviewService.createReview(request);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @Operation(summary = "Update review", description = "Updates an existing review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.reviewVerify(#id, authentication)")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequestDTO request) {
        ReviewDTO review = reviewService.updateReview(id, request);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Get review by ID", description = "Retrieves a specific review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Get review by user and product", description = "Retrieves a review for a specific user and product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/user/{userId}/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<ReviewDTO> getReviewByUserAndProduct(@PathVariable Long userId, @PathVariable Long productId) {
        ReviewDTO review = reviewService.getReviewByUserAndProduct(userId, productId);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Get reviews by product", description = "Retrieves all reviews for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Get reviews by user", description = "Retrieves all reviews written by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Delete review", description = "Permanently deletes a review (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete review by user and product", description = "Deletes a review for a specific user and product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/user/{userId}/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Void> deleteReviewByUserAndProduct(@PathVariable Long userId, @PathVariable Long productId) {
        reviewService.deleteReviewByUserAndProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get average rating", description = "Calculates average rating for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average rating calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/product/{productId}/average-rating")
    public ResponseEntity<Double> getAverageRatingForProduct(@PathVariable Long productId) {
        Double averageRating = reviewService.getAverageRatingForProduct(productId);
        return ResponseEntity.ok(averageRating);
    }

    @Operation(summary = "Get review count", description = "Gets total number of reviews for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/product/{productId}/review-count")
    public ResponseEntity<Long> getReviewCountForProduct(@PathVariable Long productId) {
        Long reviewCount = reviewService.getReviewCountForProduct(productId);
        return ResponseEntity.ok(reviewCount);
    }

    @Operation(summary = "Check if user reviewed product", description = "Checks if a user has already reviewed a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    @GetMapping("/check")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> hasUserReviewedProduct(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        Boolean hasReviewed = reviewService.hasUserReviewedProduct(userId, productId);
        return ResponseEntity.ok(hasReviewed);
    }
}