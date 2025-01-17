package br.com.ecommerce.domain.model.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

class MoneyTest {
    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Money(null), "monetary.must.not.be.null");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.00, -10.0})
    void shouldThrowExceptionWhenValueIsNegative(double doubleValue) {
        BigDecimal value = BigDecimal.valueOf(doubleValue);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Money(value), "monetary.must.be.positive");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.001, 1.0, 493.90})
    void shouldInstantiate(double value) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        Money money = new Money(bigDecimal);
        Assertions.assertNotNull(money.getValue());
    }
}