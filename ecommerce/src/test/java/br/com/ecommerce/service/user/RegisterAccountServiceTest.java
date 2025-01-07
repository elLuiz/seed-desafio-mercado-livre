package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.command.CreateAccountCommand;
import br.com.ecommerce.domain.model.permission.group.Group;
import br.com.ecommerce.domain.model.user.Password;
import br.com.ecommerce.domain.model.user.PasswordHashing;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.group.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RegisterAccountServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    GroupRepository groupRepository;
    RegisterAccountService registerAccountService;
    private PasswordHashing passwordHashing;

    @BeforeEach
    void setUp() {
        passwordHashing = new PasswordHashing() {
            @Override
            public String hash(String plainText) {
                return "RandomPassword" + plainText;
            }

            @Override
            public boolean matches(String plainText, Password password) {
                return false;
            }
        };
        this.registerAccountService = new RegisterAccountService(passwordHashing, userRepository, groupRepository);
    }

    @Test
    void shouldRegisterUser() {
        Group group = new Group("CONSUMER", "Consumer group");
        Mockito.when(groupRepository.findByAcronym("CONSUMER"))
                        .thenReturn(Optional.of(group));
        Mockito.doNothing().when(userRepository).add(new User("login@email.com", "User Name","Password@123", passwordHashing, group));
        CreateAccountCommand userCommand = new CreateAccountCommand("User Name", "login@email.com", "Password@123");
        registerAccountService.register(userCommand);

        Mockito.verify(userRepository, Mockito.times(1)).add(new User("login@email.com", "User Name", "Password@123", passwordHashing, group));
    }
}