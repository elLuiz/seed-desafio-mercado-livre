package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.service.common.Repository;

public interface ProductRepository extends Repository<Product> {
    boolean belongsTo(Long productId, String subject);
}