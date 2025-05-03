package br.com.ecommerce.domain.model.order.event;

import br.com.ecommerce.domain.model.common.DomainEvent;
import br.com.ecommerce.domain.model.order.Customer;
import br.com.ecommerce.domain.model.order.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

@Getter
public class OrderProcessed extends DomainEvent<Long> {
    private final Long customerId;
    private final String orderPurchaseId;
    private final OffsetDateTime processedAt;
    private final OffsetDateTime purchasedAt;
    private final String selectedGateway;
    private final Long productId;
    private final Integer quantity;
    private final BigDecimal price;
    private final String customerEmail;
    private final String customerName;
    
    public OrderProcessed(Order order, Customer customer) {
        super(order.getId(), Order.class.getSimpleName(), "order.processed");
        this.customerId = order.getCustomerId();
        this.orderPurchaseId = order.getPurchaseId();
        this.processedAt = order.getProcessedAt();
        this.purchasedAt = order.getPurchasedAt();
        this.selectedGateway = Optional.ofNullable(order.getPaymentGateway()).map(Enum::name).orElse(null);
        this.productId = order.getProductId();
        this.quantity = order.getQuantity();
        this.price = order.getPrice().getValue();
        this.customerEmail = customer.email();
        this.customerName = customer.fullName();
    }
}