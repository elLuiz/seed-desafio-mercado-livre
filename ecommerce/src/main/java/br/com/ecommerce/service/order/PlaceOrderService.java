package br.com.ecommerce.service.order;

import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderDetails;
import br.com.ecommerce.domain.model.order.command.CreateOrderCommand;
import br.com.ecommerce.domain.model.order.event.OrderCreated;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductStockStatus;
import br.com.ecommerce.service.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaceOrderService {
    private static final ValidationException PRODUCT_OUT_OF_STOCK = new ValidationException(ValidationErrors.single("quantity", "product.out.of.stock"));
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentMediator paymentMediator;
    private final ApplicationEventPublisher publisher;

    public PlaceOrderService(OrderRepository orderRepository,
                             ProductRepository productRepository,
                             PaymentMediator paymentMediator,
                             ApplicationEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentMediator = paymentMediator;
        this.publisher = publisher;
    }

    @Transactional
    public OrderDetails order(CreateOrderCommand createOrderCommand) {
        Product product = this.productRepository.findById(createOrderCommand.productId())
                .orElseThrow(() -> new EntityNotFoundException("product.not.found"));
        if (ProductStockStatus.OUT_OF_STOCK.equals(product.getAvailability()) || ProductStockStatus.OUT_OF_STOCK.equals(productRepository.deductStockAmount(product.getId(), createOrderCommand.quantity()))) {
            throw PRODUCT_OUT_OF_STOCK;
        }
        Order order = Order.place(createOrderCommand.sessionUser().id(), createOrderCommand.paymentGateway(), createOrderCommand.quantity(), product);
        this.orderRepository.add(order);
        this.publisher.publishEvent(new OrderCreated(order, product, this.productRepository.getOwner(product.getOwner())));
        return OrderDetails.convert(order, paymentMediator.pay(order, createOrderCommand.paymentGateway()));
    }
}