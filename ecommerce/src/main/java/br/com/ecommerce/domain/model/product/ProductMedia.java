package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.exception.ValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Objects;

@Embeddable
@Getter
public class ProductMedia {
    @Column(name = "filename", length = 255, nullable = false)
    private String fileName;
    @Column(name = "url", length = 500, nullable = false)
    private String url;
    @Column(name = "uploaded_at", columnDefinition = "timestamp with time zone")
    private OffsetDateTime uploadedAt;

    private ProductMedia() {}

    public ProductMedia(String fileName, String url, OffsetDateTime uploadedAt) {
        new ProductMediaValidator()
                .checkFileName(fileName)
                .checkURL(url)
                .evaluate()
                .orElseThrow(ValidationException::new);
        this.fileName = fileName;
        this.url = url;
        this.uploadedAt = uploadedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMedia that = (ProductMedia) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(url, that.url) && Objects.equals(uploadedAt, that.uploadedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, url, uploadedAt);
    }
}