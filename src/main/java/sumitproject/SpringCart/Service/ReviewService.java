package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.ReviewDTO;
import sumitproject.SpringCart.RequestDTO.ReviewRequestDTO;

@Component
public interface ReviewService {
    Page<ReviewDTO> getAllReviews(int pageNo, int pageSize);

    ReviewDTO createReview(Long userId, @Valid ReviewRequestDTO reviewRequestDTO);

    ReviewDTO getReviewById(Long id);

    Page<ReviewDTO> getReviewsByProductId(Long productId, int pageNo, int pageSize);

    Page<ReviewDTO> getReviewsByUserId(Long userId, int pageNo, int pageSize);

    Double getAverageRatingForProduct(Long productId);

    void deleteReviewById(Long id);

    ReviewDTO updateReviewById(Long id, @Valid ReviewRequestDTO reviewRequestDTO);

    ReviewDTO partialUpdateReviewById(Long id, ReviewRequestDTO reviewRequestDTO);
}
