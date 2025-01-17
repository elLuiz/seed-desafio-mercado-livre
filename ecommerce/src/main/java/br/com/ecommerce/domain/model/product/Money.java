package br.com.ecommerce.domain.model.product;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Getter
public class Money {
    private BigDecimal value;

    private Money() {}

    /**
     * Creates the money object
     * @param value Represents the monetary value
     * @throws IllegalArgumentException if the money is null or not positive.
     */
    public Money(BigDecimal value) {
        Assert.notNull(value, "monetary.must.not.be.null");
        Assert.isTrue(BigDecimal.ZERO.compareTo(value) < 0, "monetary.must.be.positive");
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}