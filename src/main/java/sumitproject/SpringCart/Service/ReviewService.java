package sumitproject.SpringCart.Service;

import sumitproject.SpringCart.DTO.ReviewDTO;
import sumitproject.SpringCart.RequestDTO.ReviewRequestDTO;
import org.springframework.data.domain.Page;

public interface ReviewService {

    ReviewDTO createReview(ReviewRequestDTO request);

    ReviewDTO updateReview(Long id, ReviewRequestDTO request);

    ReviewDTO getReviewById(Long id);

    ReviewDTO getReviewByUserAndProduct(Long userId, Long productId);

    Page<ReviewDTO> getReviewsByProductId(Long productId, int pageNo, int pageSize);

    Page<ReviewDTO> getReviewsByUserId(Long userId, int pageNo, int pageSize);

    void deleteReview(Long id);

    void deleteReviewByUserAndProduct(Long userId, Long productId);

    Double getAverageRatingForProduct(Long productId);

    Long getReviewCountForProduct(Long productId);

    boolean hasUserReviewedProduct(Long userId, Long productId);
}