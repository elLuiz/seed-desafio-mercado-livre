package br.com.ecommerce.infrastructure.hashing;

import br.com.ecommerce.domain.model.user.Password;
import br.com.ecommerce.domain.model.user.PasswordHashing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordHashingAlgorithm implements PasswordHashing {
    private final PasswordEncoder passwordEncoder;

    public BcryptPasswordHashingAlgorithm(@Qualifier("bcrypt") PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String plainText) {
        return passwordEncoder.encode(plainText);
    }

    @Override
    public boolean matches(String plainText, Password password) {
        return passwordEncoder.matches(plainText, password.getValue());
    }
}