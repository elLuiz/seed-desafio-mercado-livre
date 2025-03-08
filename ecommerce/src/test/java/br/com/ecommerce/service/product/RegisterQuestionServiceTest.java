package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.exception.DomainException;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.domain.model.permission.group.Group;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductCharacteristic;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import br.com.ecommerce.domain.model.product.command.RegisterQuestionCommand;
import br.com.ecommerce.domain.model.session.SessionUser;
import br.com.ecommerce.domain.model.user.PasswordHashing;
import br.com.ecommerce.domain.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class RegisterQuestionServiceTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    PasswordHashing passwordHashing;
    RegisterQuestionService registerQuestionService;

    @BeforeEach
    void setUp() {
        this.registerQuestionService = new RegisterQuestionService(productRepository);
    }

    @Test
    void shouldNotRegisterQuestionWhenAuthorIsTheProductOwner() {
        Product product = createProduct();
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        RegisterQuestionCommand registerQuestionCommand = new RegisterQuestionCommand(new SessionUser(10L, "Junit Test", Set.of("CREATE_PRODUCT", "REVIEW_PRODUCT")), 1L, "What is the content of this book?");

        Assertions.assertThrows(DomainException.class, () -> registerQuestionService.registerQuestion(registerQuestionCommand), "owner.cannot.ask.about.own.product");
    }

    @Test
    void shouldRegisterQuestionWhenAuthorIsNotTheProductOwner() {
        Product product = createProduct();
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        RegisterQuestionCommand registerQuestionCommand = new RegisterQuestionCommand(new SessionUser(12L, "Junit Test", Set.of("CREATE_PRODUCT", "REVIEW_PRODUCT")), 1L, "What is the content of this book?");
        ProductQuestion productQuestion = registerQuestionService.registerQuestion(registerQuestionCommand);

        Assertions.assertAll(() -> {
            Assertions.assertEquals("What is the content of this book?", productQuestion.getQuestion());
            Assertions.assertEquals(1L, productQuestion.getProductId());
            Assertions.assertEquals(12L, productQuestion.getUserId());
            Assertions.assertNotNull(productQuestion.getCreatedAt());
        });
    }

    Product createProduct() {
        return new Product(new Category("English"), "English for programmers", BigDecimal.valueOf(30.00), "ENP", 20, List.of(new ProductCharacteristic("pages", "2000")), 10L);
    }
}