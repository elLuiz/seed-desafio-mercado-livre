package br.com.ecommerce.infrastructure.payment;

import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderPayment;
import br.com.ecommerce.domain.model.order.PaymentGateway;
import br.com.ecommerce.service.order.Gateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.OffsetDateTime;

@Component
class PayPalGateway implements Gateway {
    private final String link;

    public PayPalGateway(@Value("${ecommerce.payment.paypal.redirect-uri}") String link) {
        this.link = link;
    }

    @Override
    public OrderPayment process(Order order) {
        String returnURL = ServletUriComponentsBuilder.fromCurrentRequest().path("/api/v1/orders/paypal/{orderId}").buildAndExpand(order.getPurchaseId()).toUri().toString();
        return new OrderPayment(order.getId(), link.replace("#orderId", order.getPurchaseId())
                .replace("#redirectURL", returnURL), OffsetDateTime.now());
    }

    @Override
    public PaymentGateway type() {
        return PaymentGateway.PAYPAL;
    }
}
