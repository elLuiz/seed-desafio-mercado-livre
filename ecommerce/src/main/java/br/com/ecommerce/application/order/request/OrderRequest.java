package br.com.ecommerce.application.order.request;

import br.com.ecommerce.domain.model.order.PaymentGateway;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(@NotNull(message = "product.must.not.be.null") Long productId,
                           @NotNull(message = "payment.gateway.must.be.selected") PaymentGateway paymentGateway,
                           @NotNull(message = "quantity.must.not.be.null")
                           @Min(value = 1, message = "quantity.must.be.positive") Integer quantity) {
}