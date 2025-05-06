package br.com.ecommerce.service.order;

import br.com.ecommerce.domain.model.order.Customer;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderStatus;
import br.com.ecommerce.domain.model.order.event.OrderFailed;
import br.com.ecommerce.domain.model.order.event.OrderProcessed;
import br.com.ecommerce.domain.model.order.exception.OrderNotFoundException;
import br.com.ecommerce.domain.model.order.exception.OrderProcessingException;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.service.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessOrderService {
    private final OrderRepository orderRepository;
    private final PaymentMediator gateway;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(ProcessOrderService.class);

    public ProcessOrderService(OrderRepository orderRepository,
                               PaymentMediator gateway,
                               ProductRepository productRepository,
                               ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.gateway = gateway;
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Processes an order with the given order ID, transaction ID and status.
     *
     * @param orderId The ID of the order to process
     * @param transactionId The transaction ID associated with the order
     * @param status The status received from the payment gateway
     * @throws OrderNotFoundException if the order cannot be found
     * @throws OrderProcessingException if the order has already been processed
     */
    @Transactional
    public void processOrder(String orderId, String transactionId, String status) {
        Order order = orderRepository.findByPurchaseId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

        if (OrderStatus.PROCESSED.equals(order.getOrderStatus())) {
            logger.warn("Attempted to process already processed order: {}, processed at: {}", orderId, order.getProcessedAt());
            throw new OrderProcessingException("order.already.processed", order.getProcessedAt(), orderId);
        }

        OrderStatus gatewayStatus = gateway.getOrderStatus(order.getPaymentGateway(), status);
        order.process(transactionId, gatewayStatus);
        publishOrderEvent(order, orderRepository.getCustomer(order.getCustomerId()).orElseThrow(() -> new EntityNotFoundException("customer.not.found")));

        orderRepository.update(order);
        logger.info("Order {} processed with status: {}", orderId, order.getOrderStatus());
    }

    /**
     * Publishes the appropriate event based on the order status.
     */
    private void publishOrderEvent(Order order, Customer customer) {
        switch (order.getOrderStatus()) {
            case PROCESSED:
                Product product = productRepository.findById(order.getProductId()).orElseThrow(() -> new EntityNotFoundException("product.not.found"));
                eventPublisher.publishEvent(new OrderProcessed(order, customer, product));
                break;
            case FAILED:
                eventPublisher.publishEvent(new OrderFailed(order));
                break;
            default:
                break;
        }
    }
}