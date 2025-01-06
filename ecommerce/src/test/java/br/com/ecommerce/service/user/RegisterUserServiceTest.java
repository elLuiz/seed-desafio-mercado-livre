package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.command.CreateUserCommand;
import br.com.ecommerce.domain.model.user.PasswordHashing;
import br.com.ecommerce.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {
    @Mock
    UserRepository userRepository;
    RegisterUserService registerUserService;
    private PasswordHashing passwordHashing;

    @BeforeEach
    void setUp() {
        passwordHashing = new PasswordHashing() {
            @Override
            public String hash(String plainText) {
                return "RandomPassword" + plainText;
            }
        };
        this.registerUserService = new RegisterUserService(passwordHashing, userRepository);
    }

    @Test
    void shouldRegisterUser() {
        Mockito.doNothing().when(userRepository).add(new User("login@email.com", "User Name","Password@123", passwordHashing));
        CreateUserCommand userCommand = new CreateUserCommand("User Name", "login@email.com", "Password@123");
        registerUserService.register(userCommand);

        Mockito.verify(userRepository, Mockito.times(1)).add(new User("login@email.com", "User Name", "Password@123", passwordHashing));
    }
}