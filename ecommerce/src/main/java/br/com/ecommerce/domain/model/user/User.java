package br.com.ecommerce.domain.model.user;

import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.GenericEntity;
import br.com.ecommerce.domain.model.permission.group.Group;
import br.com.ecommerce.domain.model.permission.role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_user")
public class User extends GenericEntity {
    @Getter
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "login", nullable = false, unique = true)
    @Email
    private String login;
    @Getter
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    @Embedded
    private Password password;
    @Getter
    @Column(name = "subject", nullable = false)
    private String subject;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_group_id", foreignKey = @ForeignKey(name = "fk_group_id"))
    private Group group;

    User() {}

    /**
     * Creates a new user
     *
     * @param login           Represents the user login, it must be a valid email address.
     * @param fullName        Represents the full name (first and last names) of the user.
     * @param password        Represents the user's password in plain-text.
     * @param passwordHashing The strategy to hash the password
     * @param group           Represents the group to which the user was assigned
     */
    public User(String login, String fullName, String password, PasswordHashing passwordHashing, Group group) {
        new UserValidator()
                .hasValidEmail(login)
                .hasValidName(fullName)
                .hasValidPassword(password)
                .hasValidGroup(group)
                .evaluate()
                .orElseThrow(ValidationException::new);
        this.login = login;
        this.fullName = fullName;
        this.createdAt = OffsetDateTime.now(ZoneId.of("UTC"));
        this.password = Password.create(password, passwordHashing);
        this.subject = UUID.randomUUID().toString();
        this.group = group;
    }

    public Set<Role> getAssignedRoles() {
        return Optional.ofNullable(group)
                .map(Group::getRoles)
                .orElse(Collections.emptySet());
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

    public boolean passwordMatches(String password, PasswordHashing passwordHashing) {
        return passwordHashing.matches(password, this.password);
    }
}