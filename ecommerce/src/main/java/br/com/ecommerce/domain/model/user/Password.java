package br.com.ecommerce.domain.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Embeddable
public class Password {
    @Column(name = "password", nullable = false)
    private String value;
    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    private Password() {}

    public static Password create(String plainPassword, PasswordHashing passwordHashing) {
        Password password = new Password();
        password.value = passwordHashing.hash(plainPassword);
        password.expiresAt = OffsetDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")).plusYears(2);
        return password;
    }

    public String getValue() {
        return value;
    }
}