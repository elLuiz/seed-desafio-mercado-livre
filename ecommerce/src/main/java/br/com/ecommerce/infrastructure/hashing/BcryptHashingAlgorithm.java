package br.com.ecommerce.infrastructure.hashing;

import br.com.ecommerce.domain.model.user.Hashing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptHashingAlgorithm implements Hashing {
    private final PasswordEncoder passwordEncoder;

    public BcryptHashingAlgorithm(@Qualifier("bcrypt") PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String plainText) {
        return passwordEncoder.encode(plainText);
    }
}