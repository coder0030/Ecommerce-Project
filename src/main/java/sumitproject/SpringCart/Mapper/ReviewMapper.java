package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.ReviewDTO;
import sumitproject.SpringCart.Entity.Product;
import sumitproject.SpringCart.Entity.Review;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.RequestDTO.ReviewRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

    public ReviewDTO toDto(Review review) {
        if (review == null) return null;

        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());

        if (review.getUser() != null) {
            dto.setUserId(review.getUser().getId());
            dto.setUserName(review.getUser().getName());
        }

        if (review.getProduct() != null) {
            dto.setProductId(review.getProduct().getId());
            dto.setProductName(review.getProduct().getName());
        }

        return dto;
    }

    public Review toEntity(ReviewRequestDTO reviewDTO, Review review) {
        if (reviewDTO == null) return null;

        if (reviewDTO.getRating() != null) {
            review.setRating(reviewDTO.getRating());
        }
        if (reviewDTO.getComment() != null) {
            review.setComment(reviewDTO.getComment());
        }

        return review;
    }

    public Review updateToEntity(ReviewRequestDTO reviewDTO, Review review) {
        boolean nullValue = false;

        if (reviewDTO.getRating() == null) nullValue = true;
        if (reviewDTO.getProductId() == null) nullValue = true;

        if (nullValue) {
            throw new BadRequestException("Incomplete data provided. Rating and product ID are required.");
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        return review;
    }
}
