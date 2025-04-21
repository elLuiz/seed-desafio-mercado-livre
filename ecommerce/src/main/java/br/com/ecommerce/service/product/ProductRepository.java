package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.model.product.Owner;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import br.com.ecommerce.domain.model.product.ProductReview;
import br.com.ecommerce.domain.model.product.ProductStockStatus;
import br.com.ecommerce.service.common.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Repository<Product> {
    boolean belongsTo(Long productId, String subject);
    void addReview(ProductReview productReview);
    boolean hasUserReviewedProduct(Long productId, Long authorId);
    Optional<Product> loadProductAttributes(Long productId);
    void addQuestion(ProductQuestion productQuestion);
    List<ProductQuestion> loadQuestionsByProductId(Long productId);
    List<ProductReview> loadReviewsByProductId(Long productId);
    ProductStockStatus deductStockAmount(Long productId, int quantity);
    Owner getOwner(Long ownerId);
}