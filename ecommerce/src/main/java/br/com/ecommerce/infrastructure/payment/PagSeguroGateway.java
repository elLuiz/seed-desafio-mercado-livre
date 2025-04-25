package br.com.ecommerce.infrastructure.payment;

import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderPayment;
import br.com.ecommerce.domain.model.order.PaymentGateway;
import br.com.ecommerce.service.order.Gateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
class PagSeguroGateway implements Gateway {
    private final String link;
    private final String callbackLink;

    public PagSeguroGateway(@Value("${ecommerce.payment.pagseguro.redirect-uri}") String link,
                            @Value("${ecommerce.payment.callback-uri}") String callbackLink) {
        this.link = link;
        this.callbackLink = callbackLink;
    }

    @Override
    public OrderPayment process(Order order) {
        return new OrderPayment(order.getId(), link.replace("#orderId", order.getPurchaseId()).replace("#redirectURL",
                callbackLink.replace("#orderId", order.getPurchaseId())),
                OffsetDateTime.now());
    }

    @Override
    public PaymentGateway type() {
        return PaymentGateway.PAG_SEGURO;
    }
}