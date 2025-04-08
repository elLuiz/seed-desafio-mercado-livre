package br.com.ecommerce.application.product.service;

import br.com.ecommerce.application.product.response.ProductResponse;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import br.com.ecommerce.domain.model.product.ProductReview;
import br.com.ecommerce.service.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViewProductService {
    private final ProductRepository productRepository;

    public ViewProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long productId) {
        Product product = this.productRepository.loadProductAttributes(productId)
                .orElseThrow(() -> new EntityNotFoundException("product.not.found"));
        List<ProductReview> reviews = this.productRepository.loadReviewsByProductId(productId);
        List<ProductQuestion> questions = this.productRepository.loadQuestionsByProductId(productId);
        return ProductResponse.convert(product, reviews, questions);
    }
}