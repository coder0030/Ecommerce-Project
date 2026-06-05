package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumitproject.SpringCart.DTO.ReviewDTO;
import sumitproject.SpringCart.Entity.Product;
import sumitproject.SpringCart.Entity.Review;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Mapper.ReviewMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DataNotFoundException;
import sumitproject.SpringCart.Repository.ProductRepository;
import sumitproject.SpringCart.Repository.ReviewRepository;
import sumitproject.SpringCart.Repository.UserRepository;
import sumitproject.SpringCart.RequestDTO.ReviewRequestDTO;
import sumitproject.SpringCart.Service.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found with id: " + userId));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));
    }

    private Review findReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Review not found with id: " + id));
    }

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewRequestDTO request) {
        User user = findUserById(request.getUserId());
        Product product = findProductById(request.getProductId());

        if (reviewRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
            throw new BadRequestException("You have already reviewed this product");
        }

        Review review = new Review();
        user.addReview(review);
        product.addReview(review);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDto(savedReview);
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long id, ReviewRequestDTO request) {
        Review review = findReviewById(id);

        if (!review.getUser().getId().equals(request.getUserId())) {
            throw new BadRequestException("You can only update your own reviews");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDto(updatedReview);
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        Review review = findReviewById(id);
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDTO getReviewByUserAndProduct(Long userId, Long productId) {
        Review review = reviewRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new DataNotFoundException("Review not found for user " + userId + " and product " + productId));
        return reviewMapper.toDto(review);
    }

    @Override
    public Page<ReviewDTO> getReviewsByProductId(Long productId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findByProductId(productId, pageable);
        return reviewPage.map(reviewMapper::toDto);
    }

    @Override
    public Page<ReviewDTO> getReviewsByUserId(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findByUserId(userId, pageable);
        return reviewPage.map(reviewMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = findReviewById(id);
        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public void deleteReviewByUserAndProduct(Long userId, Long productId) {
        if (!reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new DataNotFoundException("Review not found for user " + userId + " and product " + productId);
        }
        reviewRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public Double getAverageRatingForProduct(Long productId) {
        Double average = reviewRepository.getAverageRatingByProductId(productId);
        return average != null ? average : 0.0;
    }

    @Override
    public Long getReviewCountForProduct(Long productId) {
        return reviewRepository.getReviewCountByProductId(productId);
    }

    @Override
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        return reviewRepository.existsByUserIdAndProductId(userId, productId);
    }
}