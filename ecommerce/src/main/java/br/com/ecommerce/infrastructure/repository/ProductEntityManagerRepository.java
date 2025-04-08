package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import br.com.ecommerce.domain.model.product.ProductReview;
import br.com.ecommerce.service.product.ProductRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class ProductEntityManagerRepository extends GenericRepository<Product> implements ProductRepository {

    public static final String PRODUCT_ID = "productId";

    ProductEntityManagerRepository() {
        super(Product.class);
    }

    @Override
    public boolean belongsTo(Long productId, String subject) {
        try {
            Object result = entityManager.createNativeQuery("""
                        SELECT count(product.id) = 1 FROM {h-schema}tb_product product
                        INNER JOIN {h-schema}tb_user owner ON product.fk_user_id=owner.id
                        WHERE owner.subject=:subject AND product.id=:productId
                        """, Boolean.class)
                    .setParameter("subject", subject)
                    .setParameter(PRODUCT_ID, productId)
                    .getSingleResult();
            return Boolean.TRUE.equals(result);
        } catch (NoResultException noResultException) {
            return false;
        }
    }

    @Override
    public void addReview(ProductReview productReview) {
        entityManager.persist(productReview);
    }

    @Override
    public boolean hasUserReviewedProduct(Long productId, Long authorId) {
        try {
            Object result = entityManager.createNativeQuery("""
                        SELECT count(id) > 0 FROM {h-schema}tb_product_review 
                        WHERE fk_product_id=:productId AND fk_user_id=:authorId
                        """)
                    .setParameter(PRODUCT_ID, productId)
                    .setParameter("authorId", authorId)
                    .getSingleResult();
            return Boolean.TRUE.equals(result);
        } catch (NoResultException noResultException) {
            return false;
        }
    }

    @Override
    public void addQuestion(ProductQuestion productQuestion) {
        entityManager.persist(productQuestion);
    }

    @Override
    public Optional<Product> loadProductAttributes(Long productId) {
        String query = """
                SELECT DISTINCT product FROM Product product
                JOIN FETCH product.productCharacteristics
                JOIN FETCH product.category
                WHERE product.id = :productId
                """;
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, Product.class);
        typedQuery.setParameter(PRODUCT_ID, productId);
        Product product = typedQuery.getSingleResult();
        if (product != null) {
            Hibernate.initialize(product.getProductCharacteristics());
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<ProductQuestion> loadQuestionsByProductId(Long productId) {
        String query = """
                SELECT pq FROM ProductQuestion pq
                WHERE pq.productId=:productId
                """;
        return entityManager.createQuery(query, ProductQuestion.class)
                .setParameter(PRODUCT_ID, productId)
                .getResultList();
    }

    @Override
    public List<ProductReview> loadReviewsByProductId(Long productId) {
        String query = """
                SELECT pr FROM ProductReview pr
                WHERE pr.productId=:productId
                """;
        return entityManager.createQuery(query, ProductReview.class)
                .setParameter(PRODUCT_ID, productId)
                .getResultList();
    }
}