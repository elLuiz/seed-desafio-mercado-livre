package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.command.CreateUserCommand;
import br.com.ecommerce.domain.model.user.Hashing;
import br.com.ecommerce.domain.model.user.Password;
import br.com.ecommerce.domain.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserService {
    private final Hashing hashing;
    private final UserRepository userRepository;

    public RegisterUserService(Hashing hashing,
                               UserRepository userRepository) {
        this.hashing = hashing;
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(CreateUserCommand createUserCommand) {
        User user = new User(createUserCommand.login(), createUserCommand.fullName(), Password.create(createUserCommand.password(), hashing));
        this.userRepository.add(user);
        return user;
    }
}