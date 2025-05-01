package br.com.ecommerce.infrastructure.payment;

import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderPayment;
import br.com.ecommerce.domain.model.order.OrderStatus;
import br.com.ecommerce.domain.model.order.PaymentGateway;
import br.com.ecommerce.service.order.Gateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.OffsetDateTime;

@Component
class PagSeguroGateway implements Gateway {
    private final String link;
    public PagSeguroGateway(@Value("${ecommerce.payment.pagseguro.redirect-uri}") String link) {
        this.link = link;
    }

    @Override
    public OrderPayment process(Order order) {
        String returnURL = ServletUriComponentsBuilder.fromCurrentRequest().path("/api/v1/orders/pagseguro/{orderId}").buildAndExpand(order.getPurchaseId()).toUri().toString();
        return new OrderPayment(order.getId(), link.replace("#orderId", order.getPurchaseId()).replace("#redirectURL", returnURL),
                OffsetDateTime.now());
    }

    @Override
    public OrderStatus getOrderStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("order.status.cannot.be.empty");
        }
        return "SUCCESS".equalsIgnoreCase(status) ? OrderStatus.PROCESSED : OrderStatus.FAILED;
    }

    @Override
    public PaymentGateway type() {
        return PaymentGateway.PAG_SEGURO;
    }
}