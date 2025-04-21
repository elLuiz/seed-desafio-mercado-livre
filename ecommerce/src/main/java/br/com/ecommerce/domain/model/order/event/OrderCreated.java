package br.com.ecommerce.domain.model.order.event;

import br.com.ecommerce.domain.model.common.DomainEvent;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.product.Owner;
import br.com.ecommerce.domain.model.product.Product;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
public class OrderCreated extends DomainEvent<Long> {
    private final Long productId;
    private final String productName;
    private final Owner owner;
    private final BigDecimal price;
    private final Integer quantity;
    private final OffsetDateTime purchasedAt;

    public OrderCreated(Order order, Product product, Owner owner) {
        super(order.getId(), Order.class.getSimpleName(), "order.created");
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice().getValue();
        this.owner = owner;
        this.quantity = order.getQuantity();
        this.purchasedAt = order.getPurchasedAt();
    }
}