package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.exception.DomainException;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductReview;
import br.com.ecommerce.domain.model.product.ProductReviewAuthor;
import br.com.ecommerce.domain.model.product.command.ReviewProductCommand;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewProductService {
    private final ProductRepository productRepository;

    public ReviewProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductReview reviewProduct(ReviewProductCommand reviewProductCommand) {
        Product product = productRepository.findById(reviewProductCommand.productId())
                .orElseThrow(() -> new EntityNotFoundException("product.not.found"));
        if (productRepository.hasUserReviewedProduct(product.getId(), reviewProductCommand.author().id())) {
            throw new DomainException("author.has.already.reviewed.product");
        }
        ProductReview productReview = new ProductReview(reviewProductCommand.rating(), reviewProductCommand.title(), reviewProductCommand.description(), new ProductReviewAuthor(reviewProductCommand.author().id()), product);
        productRepository.addReview(productReview);
        return productReview;
    }
}