package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.domain.model.permission.group.Group;
import br.com.ecommerce.domain.model.product.Money;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductCharacteristic;
import br.com.ecommerce.domain.model.product.command.ProductCharacteristics;
import br.com.ecommerce.domain.model.product.command.RegisterProductCommand;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.infrastructure.hashing.BcryptPasswordHashingAlgorithm;
import br.com.ecommerce.service.category.CategoryRepository;
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
class RegisterProductServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    ProductRepository productRepository;
    RegisterProductService registerProductService;

    @BeforeEach
    void setUp() {
        this.registerProductService = new RegisterProductService(categoryRepository, productRepository);
    }

    @Test
    void shouldRegisterProduct() {
        Category category = new Category("I.T");
        Mockito.when(categoryRepository.findById(10L))
                .thenReturn(Optional.of(category));
        Mockito.doNothing().when(productRepository).add(Mockito.any(Product.class));

        RegisterProductCommand registerProductCommand = new RegisterProductCommand("Java for Kindergarten", BigDecimal.valueOf(2.50), 10, List.of(new ProductCharacteristics("Pages", "200")), "Java for young developers", 10L);
        User owner = new User("login@login.com", "Verstappen", "Asseto@23", Mockito.mock(BcryptPasswordHashingAlgorithm.class), new Group("CUSTOMER", "Customer Group"));
        Product product = registerProductService.register(registerProductCommand, owner);
        Assertions.assertAll(() -> {
            Assertions.assertEquals("Java for Kindergarten", product.getProductName());
            Assertions.assertEquals("Java for young developers", product.getDescription());
            Assertions.assertEquals(10, product.getStockQuantity());
            Assertions.assertEquals(new Money(BigDecimal.valueOf(2.50)), product.getPrice());
            Assertions.assertEquals(Set.of(new ProductCharacteristic("Pages", "200")), product.getProductCharacteristics());
        });
    }

    @Test
    void shouldNotRegisterProductWhenCategoryDoesNotExist() {
        Mockito.when(categoryRepository.findById(10L))
                .thenReturn(Optional.empty());

        RegisterProductCommand registerProductCommand = new RegisterProductCommand("Java for Kindergarten", BigDecimal.valueOf(2.50), 10, List.of(new ProductCharacteristics("Pages", "200")), "Java for young developers", 10L);
        User owner = new User("login@login.com", "Verstappen", "Asseto@23", Mockito.mock(BcryptPasswordHashingAlgorithm.class), new Group("CUSTOMER", "Customer Group"));
        Assertions.assertThrows(ValidationException.class, () -> registerProductService.register(registerProductCommand, owner));
    }
}