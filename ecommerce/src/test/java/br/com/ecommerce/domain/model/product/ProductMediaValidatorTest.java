package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.common.validation.ValidationErrors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

class ProductMediaValidatorTest {
    @ParameterizedTest
    @MethodSource("provideArgumentsAndExpectedOutcome")
    void shouldValidateProductMediaWithBadInput(String filename, String url, Set<ValidationError> expectedError) {
        ValidationErrors error = new ProductMediaValidator()
                .checkFileName(filename)
                .checkURL(url)
                .evaluate()
                .getError();

        Assertions.assertNotNull(error);
        Assertions.assertEquals(expectedError, error.getErrors());
    }

    static Stream<Arguments> provideArgumentsAndExpectedOutcome() {
        return Stream.of(
                Arguments.of("", "", Set.of(new ValidationError("filename", "filename.must.not.be.null"), new ValidationError("url", "url.must.not.be.null"))),
                Arguments.of(null, null, Set.of(new ValidationError("filename", "filename.must.not.be.null"), new ValidationError("url", "url.must.not.be.null"))),
                Arguments.of("1".repeat(256), "2".repeat(501), Set.of(new ValidationError("filename", "filename.must.not.exceed.255.characters"), new ValidationError("url", "url.must.not.exceed.500.characters")))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidInput")
    void shouldValidateProductMediaWithValidInput(String filename, String url) {
        boolean valid = new ProductMediaValidator()
                .checkFileName(filename)
                .checkURL(url)
                .evaluate()
                .valid();

        Assertions.assertTrue(valid);
    }

    static Stream<Arguments> provideValidInput() {
        return Stream.of(
                Arguments.of(UUID.randomUUID().toString(), "http://test.com.br/image/image.png"),
                Arguments.of("2".repeat(255), "http://test.com.br/very-large/%s".formatted("".repeat(400)))
        );
    }
}