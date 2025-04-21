package br.com.ecommerce.service.order;

import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderPayment;
import br.com.ecommerce.domain.model.order.PaymentGateway;
import br.com.ecommerce.domain.model.order.command.CreateOrderCommand;
import br.com.ecommerce.domain.model.product.Money;
import br.com.ecommerce.domain.model.product.Owner;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductStockStatus;
import br.com.ecommerce.domain.model.session.SessionUser;
import br.com.ecommerce.service.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class PlaceOrderServiceTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    PaymentMediator paymentMediator;
    @Mock
    ApplicationEventPublisher publisher;
    SessionUser sessionUser;
    PlaceOrderService placeOrderService;

    @BeforeEach
    void setUp() {
        this.placeOrderService = new PlaceOrderService(orderRepository, productRepository, paymentMediator, publisher);
        this.sessionUser = new SessionUser(1L, "JUNIT ORDERER", Set.of());
    }

    @Test
    void shouldNotOrderWhenProductDoesNotExist() {
        Mockito.when(productRepository.findById(10L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> placeOrderService.order(new CreateOrderCommand(sessionUser, 10L, PaymentGateway.PAG_SEGURO, 10)), "product.not.found");
    }

    @Test
    void shouldNotAllowOrderForAProductThatIsAlreadyOutOfStock() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getAvailability())
                .thenReturn(ProductStockStatus.OUT_OF_STOCK);
        Mockito.when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(sessionUser, 10L, PaymentGateway.PAYPAL, 10);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> placeOrderService.order(createOrderCommand));
        Assertions.assertEquals(Set.of(new ValidationError("quantity", "product.out.of.stock")), validationException.getErrors().getErrors());
    }

    @Test
    void shouldNotAllowOrderWhenTheStockAvailabilityIsLowerThanTheDesiredQuantity() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(10L);
        Mockito.when(product.getAvailability())
                .thenReturn(ProductStockStatus.AVAILABLE);
        Mockito.when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));
        Mockito.when(productRepository.deductStockAmount(10L, 11))
                .thenReturn(ProductStockStatus.OUT_OF_STOCK);

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(sessionUser, 10L, PaymentGateway.PAYPAL, 11);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> placeOrderService.order(createOrderCommand));
        Assertions.assertEquals(Set.of(new ValidationError("quantity", "product.out.of.stock")), validationException.getErrors().getErrors());
    }

    @Test
    void shouldPlaceOrder() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(10L);
        Mockito.when(product.getAvailability())
                .thenReturn(ProductStockStatus.AVAILABLE);
        Mockito.when(product.getPrice())
                .thenReturn(new Money(BigDecimal.valueOf(300.90)));
        Mockito.when(product.getOwner())
                .thenReturn(1L);
        Mockito.when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));
        Mockito.when(productRepository.deductStockAmount(10L, 11))
                .thenReturn(ProductStockStatus.DEDUCTED);
        Mockito.when(paymentMediator.pay(Mockito.any(Order.class), Mockito.eq(PaymentGateway.PAYPAL)))
                .thenReturn(new OrderPayment(10L, "https://fake-paypal?returnId=10L&redirectUrl=https://my-ecommerce/order/uuid", OffsetDateTime.now()));
        Mockito.when(productRepository.getOwner(1L))
                .thenReturn(new Owner(1L, "JUNIT", "junit@email.com"));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(sessionUser, 10L, PaymentGateway.PAYPAL, 11);
        Assertions.assertNotNull(placeOrderService.order(createOrderCommand));
    }
}