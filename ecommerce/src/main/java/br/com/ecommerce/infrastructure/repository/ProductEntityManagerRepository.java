package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.service.product.ProductRepository;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

@Repository
class ProductEntityManagerRepository extends GenericRepository<Product> implements ProductRepository {
    ProductEntityManagerRepository() {
        super(Product.class);
    }

    @Override
    public boolean belongsTo(Long productId, String subject) {
        try {
            Object result = entityManager.createNativeQuery("""
                        SELECT count(id) = 1 FROM {h-schema}tb_product product
                        INNER JOIN {h-schema}tb_user owner ON product.fk_user_id=owner.id
                        WHERE owner.subject=:subject AND product.id=:productId
                        """, Boolean.class)
                    .setParameter("subject", subject)
                    .setParameter("productId", productId)
                    .getSingleResult();
            return Boolean.TRUE.equals(result);
        } catch (NoResultException noResultException) {
            return false;
        }
    }
}