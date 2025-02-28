package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.common.validation.StepValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Embeddable
public class ProductReviewAuthor {
    @Column(name = "fk_user_id", nullable = false)
    private Long authorId;
    @Column(name = "review_created_at", nullable = false)
    private OffsetDateTime reviewCreation;

    ProductReviewAuthor() {}

    public ProductReviewAuthor(Long authorId) {
        StepValidator.assertTrueOrThrowValidationException(authorId != null && authorId > 0, "authorId", "author.id.must.be.valid");
        this.authorId = authorId;
        this.reviewCreation = OffsetDateTime.now(ZoneId.of("UTC"));
    }

    public OffsetDateTime getReviewCreation() {
        return reviewCreation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReviewAuthor that = (ProductReviewAuthor) o;
        return Objects.equals(authorId, that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }
}