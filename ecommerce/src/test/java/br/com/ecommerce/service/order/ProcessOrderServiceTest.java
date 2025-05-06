package br.com.ecommerce.service.order;

import br.com.ecommerce.domain.model.order.Customer;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderStatus;
import br.com.ecommerce.domain.model.order.PaymentGateway;
import br.com.ecommerce.domain.model.order.exception.OrderProcessingException;
import br.com.ecommerce.domain.model.product.Money;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.service.product.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProcessOrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    PaymentMediator paymentMediator;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @Mock
    ProductRepository productRepository;
    ProcessOrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new ProcessOrderService(orderRepository, paymentMediator, productRepository, applicationEventPublisher);
    }

    @Test
    void shouldRejectOrderWhenItHasAlreadyBeenProcessed() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(1L);
        Mockito.when(product.getPrice())
                .thenReturn(new Money(BigDecimal.valueOf(20.30)));
        Order order = Order.place(10L, PaymentGateway.PAG_SEGURO, 10, product);
        String orderId = UUID.randomUUID().toString();
        Mockito.when(orderRepository.findByPurchaseId(orderId))
                .thenReturn(Optional.of(order));
        String transactionId = UUID.randomUUID().toString();
        order.process(transactionId, OrderStatus.PROCESSED);

        Assertions.assertThrows(OrderProcessingException.class, () -> orderService.processOrder(orderId, transactionId, "0"), "order.already.processed");
    }

    @Test
    void shouldMarkOrderAsProcessed() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(1L);
        Mockito.when(product.getPrice())
                .thenReturn(new Money(BigDecimal.valueOf(20.30)));
        Order order = Order.place(10L, PaymentGateway.PAG_SEGURO, 10, product);
        String orderId = UUID.randomUUID().toString();
        Mockito.when(orderRepository.findByPurchaseId(orderId))
                .thenReturn(Optional.of(order));
        Mockito.when(paymentMediator.getOrderStatus(PaymentGateway.PAG_SEGURO, "0"))
                .thenReturn(OrderStatus.PROCESSED);
        Mockito.when(orderRepository.getCustomer(order.getCustomerId()))
                .thenReturn(Optional.of(new Customer(10L, "Customer Name", "customer@ec.com")));
        Mockito.when(productRepository.findById(order.getProductId()))
                .thenReturn(Optional.of(product));
        String transactionId = UUID.randomUUID().toString();
        orderService.processOrder(orderId, transactionId, "0");

        Assertions.assertAll(() -> {
            Assertions.assertEquals(OrderStatus.PROCESSED, order.getOrderStatus());
            Assertions.assertNotNull(order.getProcessedAt());
        });
    }

    @Test
    void shouldMarkOrderAsFailed() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(1L);
        Mockito.when(product.getPrice())
                .thenReturn(new Money(BigDecimal.valueOf(20.30)));
        Order order = Order.place(10L, PaymentGateway.PAG_SEGURO, 10, product);
        String orderId = UUID.randomUUID().toString();
        Mockito.when(orderRepository.findByPurchaseId(orderId))
                .thenReturn(Optional.of(order));
        Mockito.when(paymentMediator.getOrderStatus(PaymentGateway.PAG_SEGURO, "1"))
                .thenReturn(OrderStatus.FAILED);
        Mockito.when(orderRepository.getCustomer(order.getCustomerId()))
                .thenReturn(Optional.of(new Customer(10L, "Customer Name", "customer@ec.com")));
        String transactionId = UUID.randomUUID().toString();
        orderService.processOrder(orderId, transactionId, "1");

        Assertions.assertAll(() -> {
            Assertions.assertEquals(OrderStatus.FAILED, order.getOrderStatus());
            Assertions.assertEquals(transactionId, order.getOrderTransactionId());
            Assertions.assertNotNull(order.getProcessedAt());
        });
    }
}