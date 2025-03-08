package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.ProductQuestion;

import java.time.OffsetDateTime;

public record ProductQuestionCreatedResponse(Long id, String question, OffsetDateTime registeredAt) {
    public static ProductQuestionCreatedResponse toResponse(ProductQuestion productQuestion) {
        return new ProductQuestionCreatedResponse(productQuestion.getId(), productQuestion.getQuestion(), productQuestion.getCreatedAt());
    }
}