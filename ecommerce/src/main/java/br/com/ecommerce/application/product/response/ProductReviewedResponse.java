package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.ProductReview;

import java.time.OffsetDateTime;

public record ProductReviewedResponse(Long id, OffsetDateTime createdAt) {
    public static ProductReviewedResponse toResponse(ProductReview productReview) {
        return new ProductReviewedResponse(productReview.getId(), productReview.getAuthor().getReviewCreation());
    }
}