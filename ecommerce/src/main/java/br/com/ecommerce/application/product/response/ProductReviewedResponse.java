package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.ProductReview;

import java.time.OffsetDateTime;

public record ProductReviewedResponse(Long id, String title, String review, int rating, OffsetDateTime createdAt) {
    public static ProductReviewedResponse toResponse(ProductReview productReview) {
        return new ProductReviewedResponse(productReview.getId(), productReview.getDescription(), productReview.getTitle(), productReview.getRating(), productReview.getAuthor().getReviewCreation());
    }
}