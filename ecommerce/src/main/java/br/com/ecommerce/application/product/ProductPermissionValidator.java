package br.com.ecommerce.application.product;

import br.com.ecommerce.service.product.ProductRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class ProductPermissionValidator {
    private final ProductRepository productRepository;

    public ProductPermissionValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean isOwnerOf(Long productId, Jwt jwt) {
        return productRepository.belongsTo(productId, jwt.getSubject());
    }
}