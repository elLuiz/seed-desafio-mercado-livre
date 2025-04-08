package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.ProductReview;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
public class ReviewInformation {
    protected static final ReviewInformation EMPTY_REVIEW = new ReviewInformation(0L, BigDecimal.ZERO, List.of());
    private BigDecimal average;
    private Long amount;
    private List<ProductReviewedResponse> reviews;

    private ReviewInformation(Long amount, BigDecimal average, List<ProductReviewedResponse> reviews) {
        this.amount = amount;
        this.average = average;
        this.reviews = reviews;
    }

    public static ReviewInformation convert(List<ProductReview> productReviews) {
        if (CollectionUtils.isEmpty(productReviews)) {
            return EMPTY_REVIEW;
        }
        double reviewsAverage = calculateAverage(productReviews);
        List<ProductReviewedResponse> reviewedResponses = getProductReviewedResponses(productReviews);
        return new ReviewInformation((long) productReviews.size(), BigDecimal.valueOf(reviewsAverage).setScale(1, RoundingMode.HALF_UP), reviewedResponses);
    }

    private static double calculateAverage(List<ProductReview> productReviews) {
        return productReviews.stream()
                .mapToDouble(ProductReview::getRating)
                .average()
                .orElse(0.0);
    }

    private static List<ProductReviewedResponse> getProductReviewedResponses(List<ProductReview> productReviews) {
        return productReviews.stream().map(ProductReviewedResponse::toResponse).toList();
    }
}