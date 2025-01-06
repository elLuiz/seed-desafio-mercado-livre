package br.com.ecommerce.domain.model.user;

import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity
@Table(name = "tb_user")
public class User extends GenericEntity {
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "login", nullable = false, unique = true)
    @Email
    private String login;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    @Embedded
    private Password password;

    private User() {}

    /**
     * Creates a new user
     *
     * @param login    Represents the user login, it must be a valid email address.
     * @param fullName Represents the full name (first and last names) of the user.
     * @param password Represents the user's password in plain-text.
     * @param passwordHashing The strategy to hash the password
     */
    public User(String login, String fullName, String password, PasswordHashing passwordHashing) {
        new UserValidator()
                .hasValidEmail(login)
                .hasValidName(fullName)
                .hasValidPassword(password)
                .evaluate()
                .orElseThrow(validationErrors -> {throw new ValidationException(validationErrors);});
        this.login = login;
        this.fullName = fullName;
        this.createdAt = OffsetDateTime.now(ZoneId.of("UTC"));
        this.password = Password.create(password, passwordHashing);
    }

    public String getFullName() {
        return fullName;
    }

    public Long getId() {
        return id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}