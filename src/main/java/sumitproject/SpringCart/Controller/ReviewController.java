package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.ReviewDTO;
import sumitproject.SpringCart.RequestDTO.ReviewRequestDTO;
import sumitproject.SpringCart.Service.ReviewService;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Long userId,
                                                  @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewDTO createdReview = reviewService.createReview(userId, reviewRequestDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ReviewDTO>> getAllReviews(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<ReviewDTO> reviews = reviewService.getAllReviews(pageNo, pageSize);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO reviewDTO = reviewService.getReviewById(id);
        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByProductId(@PathVariable Long productId,
                                                                 @RequestParam(defaultValue = "0") int pageNo,
                                                                 @RequestParam(defaultValue = "20") int pageSize) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId, pageNo, pageSize);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId,
                                                              @RequestParam(defaultValue = "0") int pageNo,
                                                              @RequestParam(defaultValue = "20") int pageSize) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId, pageNo, pageSize);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}/average-rating")
    public ResponseEntity<Double> getAverageRatingForProduct(@PathVariable Long productId) {
        Double averageRating = reviewService.getAverageRatingForProduct(productId);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReviewById(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewDTO> updateReviewById(@PathVariable Long id,
                                                      @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewDTO updatedReview = reviewService.updateReviewById(id, reviewRequestDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewDTO> partialUpdateReviewById(@PathVariable Long id,
                                                             @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewDTO updatedReview = reviewService.partialUpdateReviewById(id, reviewRequestDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }
}
