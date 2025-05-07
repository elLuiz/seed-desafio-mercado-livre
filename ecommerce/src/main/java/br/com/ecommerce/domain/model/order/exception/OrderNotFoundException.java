package br.com.ecommerce.domain.model.order.exception;

import lombok.Getter;

@Getter
public class OrderNotFoundException extends RuntimeException {
    private final String orderId;

    public OrderNotFoundException(String orderId) {
        this.orderId = orderId;
    }
}
