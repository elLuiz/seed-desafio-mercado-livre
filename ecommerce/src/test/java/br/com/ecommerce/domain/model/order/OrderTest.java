package br.com.ecommerce.domain.model.order;


import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.product.Money;
import br.com.ecommerce.domain.model.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @ParameterizedTest
    @MethodSource("provideInvalidOrderParameters")
    void shouldThrowExceptionWhenOrderContainsInvalidProperties(Long customerId, PaymentGateway paymentGateway, Integer quantity, Product product, Set<String> expectedCodes) {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> Order.place(customerId, paymentGateway, quantity, product));
        Assertions.assertEquals(expectedCodes, validationException.getErrors().getErrors().stream().map(ValidationError::code).collect(Collectors.toSet()));
    }

    static Stream<Arguments> provideInvalidOrderParameters() {
        return Stream.of(
                Arguments.of(null, null, null, null, Set.of("customer.must.not.be.null", "payment.gateway.must.be.selected", "quantity.must.not.be.null", "product.not.found")),
                Arguments.of(1L, PaymentGateway.PAYPAL, -10, null, Set.of("quantity.must.be.positive", "product.not.found"))
        );
    }

    @Test
    void shouldReturnErrorWhenProductIdIsNull() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> Order.place(10L, PaymentGateway.PAYPAL, 10, product));
        Assertions.assertEquals(Set.of("product.not.found"), validationException.getErrors().getErrors().stream().map(ValidationError::code).collect(Collectors.toSet()));
    }

    @Test
    void shouldReturnErrorWhenProductDoesNotHavePrice() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(10L);
        Mockito.when(product.getPrice())
                .thenReturn(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> Order.place(10L, PaymentGateway.PAYPAL, 10, product));
        Assertions.assertEquals(Set.of("product.with.invalid.price"), validationException.getErrors().getErrors().stream().map(ValidationError::code).collect(Collectors.toSet()));

    }

    @Test
    void shouldPlaceOrder() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(10L);
        Mockito.when(product.getPrice())
                .thenReturn(new Money(BigDecimal.valueOf(10L)));
        Order order = Order.place(1L, PaymentGateway.PAG_SEGURO, 10, product);
        Assertions.assertAll(() -> {
            Assertions.assertNotNull(order);
            Assertions.assertEquals(1L, order.getCustomerId());
            Assertions.assertEquals(PaymentGateway.PAG_SEGURO, order.getPaymentGateway());
            Assertions.assertEquals(10L, order.getProductId());
            Assertions.assertEquals(new Money(BigDecimal.valueOf(10L)), order.getPrice());
            Assertions.assertEquals(OrderStatus.PAYMENT_PENDING, order.getOrderStatus());
            Assertions.assertNotNull(order.getPurchaseId());
        });
    }
}