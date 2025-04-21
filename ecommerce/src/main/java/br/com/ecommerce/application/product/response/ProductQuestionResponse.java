package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.ProductQuestion;

import java.time.OffsetDateTime;

public record ProductQuestionResponse(Long id, String question, OffsetDateTime createdAt) {
    public static ProductQuestionResponse convert(ProductQuestion productQuestion) {
        return new ProductQuestionResponse(productQuestion.getId(), productQuestion.getQuestion(), productQuestion.getCreatedAt());
    }
}