package br.com.ecommerce.domain.model.category;

import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.util.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class CategoryValidatorTest {
    @ParameterizedTest
    @MethodSource("provideInvalidCategoryNames")
    void shouldReturnErrorWhenNameViolates(String name, List<String> expectedErrors) {
        List<String> errors = new CategoryValidator()
                .validateName(name)
                .evaluate()
                .getError()
                .getErrors()
                .stream()
                .map(ValidationError::code)
                .toList();
        Assertions.assertEquals(expectedErrors, errors);
    }

    static Stream<Arguments> provideInvalidCategoryNames() {
        return Stream.of(
                Arguments.of(null, List.of("category.must.not.be.empty")),
                Arguments.of(" ", List.of("category.must.not.be.empty")),
                Arguments.of("", List.of("category.must.not.be.empty")),
                Arguments.of("A".repeat(256), List.of("category.surpasses.limit"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidCategoryNames")
    void shouldNotReturnError(String name) {
        Either<ValidationErrors, Boolean> evaluate = new CategoryValidator()
                .validateName(name)
                .evaluate();

        Assertions.assertNull(evaluate.getError());
    }

    static Stream<Arguments> provideValidCategoryNames() {
        return Stream.of(
                Arguments.of("A".repeat(255)),
                Arguments.of("Programming")
        );
    }
}