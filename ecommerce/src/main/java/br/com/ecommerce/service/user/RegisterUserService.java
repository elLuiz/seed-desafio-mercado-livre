package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.command.CreateUserCommand;
import br.com.ecommerce.domain.model.user.PasswordHashing;
import br.com.ecommerce.domain.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserService {
    private final PasswordHashing passwordHashing;
    private final UserRepository userRepository;

    public RegisterUserService(PasswordHashing passwordHashing,
                               UserRepository userRepository) {
        this.passwordHashing = passwordHashing;
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(CreateUserCommand createUserCommand) {
        User user = new User(createUserCommand.login(), createUserCommand.fullName(), createUserCommand.password(), passwordHashing);
        this.userRepository.add(user);
        return user;
    }
}