package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.service.order.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
class OrderEntityManagerRepository extends GenericRepository<Order> implements OrderRepository {
    public OrderEntityManagerRepository() {
        super(Order.class);
    }
}