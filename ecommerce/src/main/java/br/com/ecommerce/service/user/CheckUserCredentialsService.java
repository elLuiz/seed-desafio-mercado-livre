package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.domain.exception.ResourceNotFound;
import br.com.ecommerce.domain.model.user.PasswordHashing;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.util.Either;
import br.com.ecommerce.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckUserCredentialsService {
    private final UserRepository userRepository;
    private final PasswordHashing passwordHashing;

    public CheckUserCredentialsService(UserRepository userRepository, PasswordHashing passwordHashing) {
        this.userRepository = userRepository;
        this.passwordHashing = passwordHashing;
    }

    @Transactional(readOnly = true)
    public Either<ValidationErrors, User> loadByLoginAndPassword(String login, String password) {
        if (StringUtils.isNullOrEmpty(login)) {
            return Either.error(ValidationErrors.single("login", "login.must.not.be.empty"));
        } else if (StringUtils.isNullOrEmpty(password)) {
            return Either.error(ValidationErrors.single("password", "password.must.not.be.empty"));
        }
        User user = userRepository.findByLogin(login.trim()).orElseThrow(() -> new ResourceNotFound("user.not.found"));
        if (!user.passwordMatches(password, passwordHashing)) {
            return Either.error(ValidationErrors.single("login", "login.or.password.invalid"));
        }
        return Either.correct(user);
    }
}