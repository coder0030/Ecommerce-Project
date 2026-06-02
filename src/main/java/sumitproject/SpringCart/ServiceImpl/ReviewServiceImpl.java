package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.ReviewDTO;
import sumitproject.SpringCart.RequestDTO.ReviewRequestDTO;
import sumitproject.SpringCart.Service.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    @Override
    public Page<ReviewDTO> getAllReviews(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public ReviewDTO createReview(Long userId, ReviewRequestDTO reviewRequestDTO) {
        return null;
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        return null;
    }

    @Override
    public Page<ReviewDTO> getReviewsByProductId(Long productId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<ReviewDTO> getReviewsByUserId(Long userId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Double getAverageRatingForProduct(Long productId) {
        return 0.0;
    }

    @Override
    public void deleteReviewById(Long id) {

    }

    @Override
    public ReviewDTO updateReviewById(Long id, ReviewRequestDTO reviewRequestDTO) {
        return null;
    }

    @Override
    public ReviewDTO partialUpdateReviewById(Long id, ReviewRequestDTO reviewRequestDTO) {
        return null;
    }
}
