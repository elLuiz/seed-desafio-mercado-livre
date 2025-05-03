package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.order.Customer;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.service.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class OrderEntityManagerRepository extends GenericRepository<Order> implements OrderRepository {
    public OrderEntityManagerRepository() {
        super(Order.class);
    }

    @Override
    public Optional<Order> findByPurchaseId(String purchaseId) {
        String query = """
                SELECT order FROM Order order
                WHERE order.purchaseId=:purchaseId
                """;
        return Optional.ofNullable(entityManager.createQuery(query, Order.class)
                .setParameter("purchaseId", purchaseId)
                .getSingleResult());
    }

    @Override
    public Optional<Customer> getCustomer(Long customerId) {
        return Optional.ofNullable(entityManager.createQuery("""
                SELECT new br.com.ecommerce.domain.model.order.Customer(id, fullName, login AS email) FROM User
                WHERE id = :customerId
                """, Customer.class)
                .setParameter("customerId", customerId)
                .getSingleResult());
    }
}