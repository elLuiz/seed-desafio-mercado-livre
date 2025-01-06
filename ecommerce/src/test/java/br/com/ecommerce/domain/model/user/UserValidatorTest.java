package br.com.ecommerce.domain.model.user;

import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.util.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Stream;

class UserValidatorTest {
    UserValidator userValidator = new UserValidator();

    @ParameterizedTest
    @ValueSource(strings = {"email@mail", "@email", "email", ".com.br", ".email@gmail.com"})
    void shouldReturnErrorWhenEmailIsInvalid(String email) {
        Either<ValidationErrors, Boolean> evaluate = userValidator.hasValidEmail(email).evaluate();
        Assertions.assertEquals(Set.of(new ValidationError("email", "login.must.not.be.invalid.email")), evaluate.getError().getErrors());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void shouldReturnErrorWhenEmailIsNullOrEmpty(String email) {
        Either<ValidationErrors, Boolean> evaluate = userValidator.hasValidEmail(email).evaluate();
        Assertions.assertEquals(Set.of(new ValidationError("email", "login.must.not.be.null")), evaluate.getError().getErrors());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void shouldReturnErrorWhenNameIsNullOrEmpty(String name) {
        Either<ValidationErrors, Boolean> evaluate = userValidator.hasValidName(name).evaluate();
        Assertions.assertEquals(Set.of(new ValidationError("fullName", "full.name.must.not.be.null")), evaluate.getError().getErrors());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void shouldReturnErrorWhenNameIsInvalid(String name, String expectedError) {
        Either<ValidationErrors, Boolean> evaluate = userValidator.hasValidName(name).evaluate();
        Assertions.assertEquals(Set.of(new ValidationError("fullName", expectedError)), evaluate.getError().getErrors());
    }

    static Stream<Arguments> provideArguments() {
        return Stream.of(
                Arguments.of("anmh".repeat(31), "full.name.must.not.surpass.120.characters"),
                Arguments.of("NAME209320932", "name.must.not.violate.pattern"),
                Arguments.of("2832981", "name.must.not.violate.pattern")
        );
    }

    @ParameterizedTest
    @MethodSource("providePasswords")
    void shouldReturnErrorWhenPasswordIsInvalid(String password, String expectedError) {
        Either<ValidationErrors, Boolean> evaluate = userValidator.hasValidPassword(password).evaluate();
        Assertions.assertEquals(Set.of(new ValidationError("password", expectedError)), evaluate.getError().getErrors());
    }

    static Stream<Arguments> providePasswords() {
        return Stream.of(
                Arguments.of("anmh".repeat(31), "password.must.not.violate.pattern"),
                Arguments.of("   ", "password.must.not.be.null"),
                Arguments.of(null, "password.must.not.be.null"),
                Arguments.of("2832981", "password.must.not.violate.pattern"),
                Arguments.of("Ajskjuiuiwu", "password.must.not.violate.pattern"),
                Arguments.of("891AJKJJAak", "password.must.not.violate.pattern"),
                Arguments.of("@1198209209", "password.must.not.violate.pattern")
        );
    }

    @ParameterizedTest
    @MethodSource("provideCompliantArguments")
    void shouldProvideCompliantArguments(String name, String email, String password) {
        Assertions.assertTrue(userValidator.hasValidName(name)
                .hasValidEmail(email)
                .hasValidPassword(password)
                .evaluate()
                .orElseThrow(validationErrors -> {throw new IllegalArgumentException();}));

    }

    static Stream<Arguments> provideCompliantArguments() {
        return Stream.of(
                Arguments.of("Luís Suarez", "suarito@sua.com", "J@>p4ssword"),
                Arguments.of("João Félix", "felix2@madrid.com", "passoW@102"),
                Arguments.of("Verstappen", "verstap@vers.com", "World@2024"),
                Arguments.of("Ver".repeat(40), "verstap@vers.com", "world@20243sngjJ")
        );
    }
}