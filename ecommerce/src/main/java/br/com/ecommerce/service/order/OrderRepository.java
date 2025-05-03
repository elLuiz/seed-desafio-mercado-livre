package br.com.ecommerce.service.order;

import br.com.ecommerce.domain.model.order.Customer;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.service.common.Repository;

import java.util.Optional;

public interface OrderRepository extends Repository<Order> {
    Optional<Order> findByPurchaseId(String purchaseId);
    Optional<Customer> getCustomer(Long orderId);
}