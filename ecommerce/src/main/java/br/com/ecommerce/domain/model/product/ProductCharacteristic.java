package br.com.ecommerce.domain.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
public class ProductCharacteristic {
    @Column(name = "property", nullable = false, length = 100)
    private String property;
    @Column(name = "property_value", nullable = false, length = 100)
    private String value;

    ProductCharacteristic() {
    }

    public ProductCharacteristic(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductCharacteristic that = (ProductCharacteristic) o;
        return Objects.equals(getProperty(), that.getProperty()) && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProperty(), getValue());
    }
}