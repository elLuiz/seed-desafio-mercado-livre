package br.com.ecommerce.service.order;

import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderPayment;
import br.com.ecommerce.domain.model.order.PaymentGateway;

public interface Gateway {
    OrderPayment process(Order order);
    PaymentGateway type();
}
