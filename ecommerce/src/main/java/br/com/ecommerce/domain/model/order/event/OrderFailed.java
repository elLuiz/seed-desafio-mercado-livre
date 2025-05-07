package br.com.ecommerce.domain.model.order.event;

import br.com.ecommerce.domain.model.common.DomainEvent;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderPayment;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Optional;

@Getter
public class OrderFailed extends DomainEvent<Long> {
    private final Long customerId;
    private final OffsetDateTime processedAt;
    private final OffsetDateTime purchasedAt;
    private final String selectedGateway;
    private final Long productId;
    private final OrderPayment orderPayment;
    private final String purchaseId;

    public OrderFailed(Order order, OrderPayment orderPayment) {
        super(order.getId(), Order.class.getSimpleName(), "order.failed");
        this.customerId = order.getCustomerId();
        this.processedAt = order.getProcessedAt();
        this.purchasedAt = order.getPurchasedAt();
        this.selectedGateway = Optional.ofNullable(order.getPaymentGateway()).map(Enum::name).orElse(null);
        this.productId = order.getProductId();
        this.orderPayment = orderPayment;
        this.purchaseId = order.getPurchaseId();
    }
}