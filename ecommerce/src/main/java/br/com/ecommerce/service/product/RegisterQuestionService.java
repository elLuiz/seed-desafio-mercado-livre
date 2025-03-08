package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.exception.DomainException;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import br.com.ecommerce.domain.model.product.command.RegisterQuestionCommand;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterQuestionService {
    private final ProductRepository productRepository;

    public RegisterQuestionService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductQuestion registerQuestion(RegisterQuestionCommand registerQuestionCommand) {
        Product product = productRepository.findById(registerQuestionCommand.productId())
                .orElseThrow(() -> new EntityNotFoundException("product.not.found"));
        if (product.isOwnedBy(registerQuestionCommand.user().id())) {
            throw new DomainException("owner.cannot.ask.about.own.product");
        }
        ProductQuestion productQuestion = new ProductQuestion(registerQuestionCommand.question(), registerQuestionCommand.productId(), registerQuestionCommand.user().id());
        productRepository.addQuestion(productQuestion);
        return productQuestion;
    }
}