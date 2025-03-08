package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.model.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "tb_product_question")
public class ProductQuestion extends GenericEntity {
    @Column(name = "question", nullable = false, length = 300)
    private String question;
    @Column(name = "fk_product_id", nullable = false)
    private Long productId;
    @Column(name = "fk_user_id", nullable = false)
    private Long userId;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public ProductQuestion(String question, Long productId, Long userId) {
        this.question = question;
        this.productId = productId;
        this.userId = userId;
        this.createdAt = OffsetDateTime.now(ZoneId.of("UTC"));
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getProductId() {
        return productId;
    }

    public String getQuestion() {
        return question;
    }

    public Long getUserId() {
        return userId;
    }
}