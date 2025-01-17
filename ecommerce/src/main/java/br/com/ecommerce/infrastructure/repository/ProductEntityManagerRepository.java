package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.service.product.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
class ProductEntityManagerRepository extends GenericRepository<Product> implements ProductRepository {
    ProductEntityManagerRepository() {
        super(Product.class);
    }
}