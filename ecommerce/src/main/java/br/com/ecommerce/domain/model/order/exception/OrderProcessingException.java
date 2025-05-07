package br.com.ecommerce.domain.model.order.exception;

import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class OrderProcessingException extends RuntimeException {
    private final OffsetDateTime processedAt;
    private final String orderId;

    public OrderProcessingException(String message, OffsetDateTime processedAt, String orderId) {
        super(message);
        this.processedAt = processedAt;
        this.orderId = orderId;
    }
}
