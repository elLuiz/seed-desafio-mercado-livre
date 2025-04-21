package br.com.ecommerce.domain.model.order;

import java.time.OffsetDateTime;

public record OrderPayment(Long orderId, String redirectURI, OffsetDateTime generatedAt) {
}