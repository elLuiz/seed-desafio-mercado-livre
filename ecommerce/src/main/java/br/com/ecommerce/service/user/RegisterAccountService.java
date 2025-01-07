package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.command.CreateAccountCommand;
import br.com.ecommerce.domain.exception.ResourceNotFound;
import br.com.ecommerce.domain.model.permission.group.Group;
import br.com.ecommerce.domain.model.user.PasswordHashing;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.group.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterAccountService {
    private final PasswordHashing passwordHashing;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public RegisterAccountService(PasswordHashing passwordHashing,
                                  UserRepository userRepository,
                                  GroupRepository groupRepository) {
        this.passwordHashing = passwordHashing;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public User register(CreateAccountCommand createAccountCommand) {
        Group group = groupRepository.findByAcronym("CONSUMER").orElseThrow(() -> new ResourceNotFound("group.not.found"));
        User user = new User(createAccountCommand.login(), createAccountCommand.fullName(), createAccountCommand.password(), passwordHashing, group);
        this.userRepository.add(user);
        return user;
    }
}