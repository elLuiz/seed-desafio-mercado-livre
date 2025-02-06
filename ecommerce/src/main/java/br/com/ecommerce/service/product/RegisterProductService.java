package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.category.CategoryRepository;
import br.com.ecommerce.domain.model.product.command.RegisterProductCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public RegisterProductService(CategoryRepository categoryRepository,
                                  ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Product register(RegisterProductCommand registerProductCommand, User owner) {
        Category category = categoryRepository.findById(registerProductCommand.categoryId()).orElseThrow(() -> new ValidationException(ValidationErrors.single("categoryId", "category.not.found")));
        Product product = new Product(category, registerProductCommand.description(), registerProductCommand.price(), registerProductCommand.name(), registerProductCommand.stockQuantity(), registerProductCommand.convertCharacteristics(), owner);
        productRepository.add(product);
        return product;
    }
}