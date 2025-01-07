package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.exception.ResourceNotFound;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.user.PasswordHashing;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.infrastructure.hashing.BcryptPasswordHashingAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class CheckUserCredentialsServiceTest {
    @Mock
    UserRepository userRepository;
    PasswordHashing passwordHashing = new BcryptPasswordHashingAlgorithm(new BCryptPasswordEncoder());
    CheckUserCredentialsService checkUserCredentialsService;

    @BeforeEach
    void setUp() {
        this.checkUserCredentialsService = new CheckUserCredentialsService(userRepository, passwordHashing);
    }

    @ParameterizedTest
    @MethodSource("provideEmptyParameters")
    void shouldReturnValidationErrorWhenLoginOrPasswordIsEmpty(String login, String password, ValidationError expectedError) {
        Set<ValidationError> validationError = checkUserCredentialsService.loadByLoginAndPassword(login, password)
                .getError()
                .getErrors();

        Assertions.assertEquals(Set.of(expectedError), validationError);
    }

    static Stream<Arguments> provideEmptyParameters() {
        return Stream.of(
            Arguments.of("", "password@12A", new ValidationError("login", "login.must.not.be.empty")),
            Arguments.of("login@login.com", "", new ValidationError("password", "password.must.not.be.empty")),
            Arguments.of(null, "password@12A", new ValidationError("login", "login.must.not.be.empty")),
            Arguments.of("login@login.com", null, new ValidationError("password", "password.must.not.be.empty"))
        );
    }

    @Test
    void shouldThrowExceptionWhenUserLoginDoesNotExist() {
        Mockito.when(userRepository.findByLogin("login@login.com"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFound.class, () -> checkUserCredentialsService.loadByLoginAndPassword("login@login.com", "World@20"), "user.not.found");
    }

    @Test
    void shouldReturnErrorWhenPasswordDoesNotMatch() {
        User user = new User("login@login.com", "Max Verstappen", "World@2024", passwordHashing);
        Mockito.when(userRepository.findByLogin("login@login.com"))
                .thenReturn(Optional.of(user));
        Set<ValidationError> validationErrors = checkUserCredentialsService.loadByLoginAndPassword("login@login.com", "World@2025")
                .getError()
                .getErrors();

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals(new ValidationError("login", "login.or.password.invalid"), validationErrors.stream().findFirst().orElse(null));
    }

    @Test
    void shouldReturnUserWhenPasswordMatches() {
        User user = new User("login@login.com", "Max Verstappen", "World@2024", passwordHashing);
        Mockito.when(userRepository.findByLogin("login@login.com"))
                .thenReturn(Optional.of(user));
        User returnedUser = checkUserCredentialsService.loadByLoginAndPassword("login@login.com", "World@2024")
                .orElseThrow(ValidationException::new);

        Assertions.assertNotNull(returnedUser);
    }
}