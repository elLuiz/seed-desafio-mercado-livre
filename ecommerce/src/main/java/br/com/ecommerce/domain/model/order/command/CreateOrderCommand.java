package br.com.ecommerce.domain.model.order.command;

import br.com.ecommerce.domain.model.order.PaymentGateway;
import br.com.ecommerce.domain.model.session.SessionUser;

public record CreateOrderCommand(SessionUser sessionUser, Long productId, PaymentGateway paymentGateway, Integer quantity) {
}