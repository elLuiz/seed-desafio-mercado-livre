package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.domain.model.permission.group.Group;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.infrastructure.hashing.BcryptPasswordHashingAlgorithm;
import br.com.ecommerce.util.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ProductValidatorTest {
    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void shouldReturnErrorsForInvalidInput(String description, BigDecimal price, String name, Integer quantity, Category category, List<ProductCharacteristic> characteristics, Long owner, Set<String> expectedErrors) {
        Either<ValidationErrors, Boolean> evaluate = new ProductValidator()
                .checkValidDescription(description)
                .checkValidPrice(price)
                .checkValidName(name)
                .checkValidStockQuantity(quantity)
                .checkValidCategory(category)
                .checkCharacteristics(characteristics)
                .checkOwner(owner)
                .evaluate();

        Assertions.assertFalse(evaluate.valid());
        Assertions.assertEquals(expectedErrors, evaluate.getError().getErrors().stream().map(ValidationError::code).collect(Collectors.toSet()));
    }

    static Stream<Arguments> provideInvalidData() {
        return Stream.of(
                Arguments.of("", null, null, null, null, getCharacteristics(new ProductCharacteristic("", ""), new ProductCharacteristic("a".repeat(101), "a".repeat(101))), null, Set.of("description.must.not.be.empty", "price.must.not.be.null", "product.name.must.not.be.empty", "quantity.must.not.be.empty", "category.must.not.be.null", "owner.must.not.be.null", "property.value.must.not.be.empty", "property.value.must.not.surpass.100.characters", "property.must.not.be.empty", "property.must.not.surpass.100.characters")),
                Arguments.of("Description".repeat(1000), BigDecimal.valueOf(0.0), "Name".repeat(200), 0, new Category("CATEGORY NAME"), getCharacteristics(), null, Set.of("product.name.must.not.surpass.255.characters", "quantity.must.be.positive", "invalid.price", "description.must.not.surpass.1000.characters", "owner.must.not.be.null", "characteristics.must.be.specified")),
                Arguments.of("Description", BigDecimal.valueOf(1.99), "Name", -10, new Category("CATEGORY NAME"), null, null, Set.of("invalid.price", "quantity.must.be.positive", "owner.must.not.be.null", "characteristics.must.be.specified"))
        );
    }

    static List<ProductCharacteristic> getCharacteristics(ProductCharacteristic ...characteristics) {
        return List.of(characteristics);
    }

    @Test
    void shouldReturnTrueWhenProductInfoIsValid() {
        Either<ValidationErrors, Boolean> evaluate = new ProductValidator()
                .checkValidDescription("Product Description")
                .checkValidPrice(BigDecimal.valueOf(2.00))
                .checkValidName("Name")
                .checkValidStockQuantity(1)
                .checkValidCategory(new Category("Informatics"))
                .checkCharacteristics(getCharacteristics(new ProductCharacteristic("Language", "Java, C#")))
                .checkOwner(10L)
                .evaluate();

        Assertions.assertTrue(evaluate.valid());
    }
}