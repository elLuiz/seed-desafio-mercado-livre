package br.com.ecommerce.application.login;

import br.com.ecommerce.application.login.dto.LoginResponse;
import br.com.ecommerce.domain.model.user.User;

public interface TokenGenerator {
    LoginResponse generateToken(User user);
}
