package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.common.validation.ValidationErrors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ProductReviewValidatorTest {
    @ParameterizedTest
    @MethodSource("provideInvalidReviews")
    void shouldReturnErrorWhenReviewIsInvalid(Integer rating, String title, String description, ProductReviewAuthor author, Product product, Set<String> expectedErrors) {
        ValidationErrors error = new ProductReviewValidator()
                .checkRating(rating)
                .checkTitle(title)
                .checkDescription(description)
                .checkAuthor(author)
                .checkProduct(product)
                .evaluate()
                .getError();

        Assertions.assertNotNull(error);
        Assertions.assertEquals(expectedErrors, error.getErrors().stream().map(ValidationError::code).collect(Collectors.toSet()));
    }

    static Stream<Arguments> provideInvalidReviews() {
        return Stream.of(
                Arguments.of(null, "", "", null, null, Set.of("rating.must.not.be.null", "title.must.not.be.empty", "description.must.not.be.empty", "author.must.not.be.null", "product.must.not.be.null")),
                Arguments.of(-1, "A".repeat(256), "B".repeat(501), new ProductReviewAuthor(1L), Mockito.mock(Product.class), Set.of("rating.minimum.must.not.be.lesser.than.one", "title.must.not.exceed.255.characters", "description.must.not.exceed.500.characters")),
                Arguments.of(6, "A".repeat(255), "B".repeat(500), new ProductReviewAuthor(1L), Mockito.mock(Product.class), Set.of("rating.maximum.must.not.be.greater.than.five"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidReviews")
    void shouldReturnTrueWhenReviewIsValid(Integer rating, String title, String description, ProductReviewAuthor author, Product product) {
        boolean valid = new ProductReviewValidator()
                .checkRating(rating)
                .checkTitle(title)
                .checkDescription(description)
                .checkAuthor(author)
                .checkProduct(product)
                .evaluate()
                .valid();

        Assertions.assertTrue(valid);
    }

    static Stream<Arguments> provideValidReviews() {
        return Stream.of(
                Arguments.of(4, "Excellent", "A really outstanding product", new ProductReviewAuthor(30L), Mockito.mock(Product.class)),
                Arguments.of(5, "E".repeat(255), "A".repeat(500), new ProductReviewAuthor(30L), Mockito.mock(Product.class)),
                Arguments.of(1, "E".repeat(255), "A".repeat(500), new ProductReviewAuthor(30L), Mockito.mock(Product.class))
        );
    }
}