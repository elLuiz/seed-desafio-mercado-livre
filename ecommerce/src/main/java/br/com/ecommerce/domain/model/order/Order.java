package br.com.ecommerce.domain.model.order;

import br.com.ecommerce.domain.model.GenericEntity;
import br.com.ecommerce.domain.model.product.Money;
import br.com.ecommerce.domain.model.product.Product;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Entity
@Table(name = "tb_order")
public class Order extends GenericEntity {
    @Column(name = "fk_customer_id", nullable = false)
    private Long customerId;
    @Column(name = "purchased_at")
    private OffsetDateTime purchasedAt;
    @Column(name = "selected_gateway", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;
    @Column(name = "fk_product_id", nullable = false)
    private Long productId;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Money price;
    @Column(name = "order_unique_id", nullable = false)
    private String uniqueId;

    public Order(Long customerId, PaymentGateway paymentGateway, Integer quantity, Product product) {
        this.customerId = customerId;
        this.paymentGateway = paymentGateway;
        this.productId = product.getId();
        this.price = product.getPrice();
        this.quantity = quantity;
        this.purchasedAt = OffsetDateTime.now(ZoneId.of("UTC"));
        this.uniqueId = UUID.randomUUID().toString();
    }
}