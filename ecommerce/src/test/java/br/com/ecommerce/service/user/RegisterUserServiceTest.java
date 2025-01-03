package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.command.CreateUserCommand;
import br.com.ecommerce.domain.model.user.Hashing;
import br.com.ecommerce.domain.model.user.Password;
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
    private Hashing hashing;

    @BeforeEach
    void setUp() {
        hashing = new Hashing() {
            @Override
            public String hash(String plainText) {
                return "RandomPassword" + plainText;
            }
        };
        this.registerUserService = new RegisterUserService(hashing, userRepository);
    }

    @Test
    void shouldRegisterUser() {
        Mockito.doNothing().when(userRepository).add(new User("login@email.com", "User Name", Password.create("Password@123", hashing)));
        CreateUserCommand userCommand = new CreateUserCommand("User Name", "login@email.com", "Password@123");
        registerUserService.register(userCommand);

        Mockito.verify(userRepository, Mockito.times(1)).add(new User("login@email.com", "User Name", Password.create("Password@123", hashing)));
    }
}