package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.GenericEntity;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.domain.model.user.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_product")
public class Product extends GenericEntity {
    @Column(name = "name", length = 255)
    @NotBlank
    private String productName;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Money price;
    @Column(name = "stock_quantity")
    private Integer stockQuantity;
    @Column(name = "description", length = 1_000, nullable = false)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_category_id")
    private Category category;
    @CollectionTable(name = "tb_product_characteristic", joinColumns = @JoinColumn(name = "fk_product_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<ProductCharacteristic> productCharacteristics;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id")
    private User owner;
    @CollectionTable(name = "tb_product_media", joinColumns = @JoinColumn(name = "fk_product_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<ProductMedia> medias;

    Product() {}

    public Product(Category category,
                   String description,
                   BigDecimal price,
                   String productName,
                   Integer stockQuantity,
                   List<ProductCharacteristic> productCharacteristics,
                   User owner) {
        new ProductValidator()
                .checkValidDescription(description)
                .checkValidPrice(price)
                .checkValidName(productName)
                .checkValidStockQuantity(stockQuantity)
                .checkValidCategory(category)
                .checkCharacteristics(productCharacteristics)
                .checkOwner(owner)
                .evaluate()
                .orElseThrow(ValidationException::new);
        setCategory(category);
        setDescription(description);
        setPrice(new Money(price));
        setProductName(productName);
        setStockQuantity(stockQuantity);
        setProductCharacteristics(productCharacteristics);
        setOwner(owner);
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    private void setDescription(String description) {
        this.description = description.trim();
    }

    private void setPrice(Money price) {
        this.price = price;
    }

    private void setProductName(String productName) {
        this.productName = productName.trim();
    }

    private void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    private void setProductCharacteristics(List<ProductCharacteristic> productCharacteristics) {
        this.productCharacteristics = new HashSet<>(productCharacteristics);
    }

    private void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public Set<ProductCharacteristic> getProductCharacteristics() {
        return productCharacteristics;
    }

    public @NotBlank String getProductName() {
        return productName;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public User getOwner() {
        return owner;
    }

    public void addMedia(ProductMedia media) {
        if (this.medias == null) {
            this.medias = new HashSet<>();
        }
        this.medias.add(media);
    }

    public Set<ProductMedia> getMedias() {
        return medias;
    }

    public boolean isOwnedBy(Long userId) {
        return this.getOwner().getId().equals(userId);
    }
}