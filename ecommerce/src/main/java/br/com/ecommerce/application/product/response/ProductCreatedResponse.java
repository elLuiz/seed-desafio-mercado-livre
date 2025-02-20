package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.Product;

import java.time.OffsetDateTime;

public record ProductCreatedResponse(Long productId, OffsetDateTime registeredAt) {
    public static ProductCreatedResponse toResponse(Product product) {
        return new ProductCreatedResponse(product.getId(), OffsetDateTime.now());
    }
}