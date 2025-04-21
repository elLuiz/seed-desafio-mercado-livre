package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "tb_product_review")
@Getter
public class ProductReview extends GenericEntity {
    @Column(name = "rating", nullable = false)
    private int rating;
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    @Column(name = "description", nullable = false, length = 500)
    private String description;
    @Embedded
    private ProductReviewAuthor author;
    @Column(name = "fk_product_id", nullable = false)
    private Long productId;

    ProductReview() {}

    public ProductReview(Integer rating, String title, String description, ProductReviewAuthor author, Product product) {
        new ProductReviewValidator()
                .checkRating(rating)
                .checkTitle(title)
                .checkDescription(description)
                .checkAuthor(author)
                .checkProduct(product)
                .evaluate()
                .orElseThrow(ValidationException::new);
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.author = author;
        this.productId = product.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReview that = (ProductReview) o;
        return rating == that.rating && Objects.equals(author, that.author) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, author, productId);
    }
}