package br.com.ecommerce.domain.model.order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record OrderDetails(String redirectURI, BigDecimal total, OffsetDateTime registeredAt) {
    public static OrderDetails convert(Order order, OrderPayment orderPayment) {
        return new OrderDetails(orderPayment.redirectURI(), order.getPrice().multiply(order.getQuantity()).getValue(), order.getPurchasedAt());
    }
}