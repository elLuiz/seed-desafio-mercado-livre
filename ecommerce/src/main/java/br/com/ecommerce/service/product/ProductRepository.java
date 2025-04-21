package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.model.product.Owner;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import br.com.ecommerce.domain.model.product.ProductReview;
import br.com.ecommerce.domain.model.product.ProductStockStatus;
import br.com.ecommerce.service.common.Repository;

public interface ProductRepository extends Repository<Product> {
    boolean belongsTo(Long productId, String subject);
    void addReview(ProductReview productReview);
    boolean hasUserReviewedProduct(Long productId, Long authorId);
    void addQuestion(ProductQuestion productQuestion);
    ProductStockStatus deductStockAmount(Long productId, int quantity);
    Owner getOwner(Long ownerId);
}