package br.com.ecommerce.domain.model.category;

import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Table(name = "tb_category")
@Entity
public class Category extends GenericEntity {
    @Column(name = "category_name", nullable = false, length = 255)
    @Size(max = 255)
    @NotBlank
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_category_id", foreignKey = @ForeignKey(name = "fk_category_id"))
    private Category parent;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CategoryStatus categoryStatus;

    Category() {}

    public Category(String name) {
        new CategoryValidator()
                .validateName(name)
                .evaluate()
                .orElseThrow(ValidationException::new);
        this.name = name.trim();
        this.createdAt = OffsetDateTime.now(ZoneId.of("UTC"));
        this.categoryStatus = CategoryStatus.ACTIVE;
    }

    /**
     * Assign a parent category to the current instance.
     * @param parent  The parent category
     */
    public void childOf(Category parent) {
        if (parent != null) {
            this.parent = parent;
        }
    }

    public CategoryStatus getCategoryStatus() {
        return categoryStatus;
    }

    public String getName() {
        return name;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Category getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean isActive() {
        return CategoryStatus.ACTIVE == this.categoryStatus;
    }
}