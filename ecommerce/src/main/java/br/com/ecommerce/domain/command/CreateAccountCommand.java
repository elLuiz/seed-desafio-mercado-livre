package br.com.ecommerce.domain.command;

import br.com.ecommerce.domain.common.unique.Unique;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.domain.model.user.UserValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAccountCommand(@NotBlank(message = "full.name.must.not.be.null")
                                @Size(max = 120, message = "full.name.must.not.surpass.120.characters")
                                @Pattern(regexp = UserValidationConstants.NAME_EXPRESSION, message = "name.must.not.violate.pattern") String fullName,
                                   @NotBlank(message = "login.must.not.be.null")
                                @Email(message = "login.must.not.be.invalid.email")
                                @Unique(ownerClass = User.class, name = "login", message = "login.must.be.unique") String login,
                                   @NotBlank(message = "password.must.not.be.null")
                                @Pattern(regexp = UserValidationConstants.PASSWORD_EXPRESSION, message = "password.must.not.violate.pattern") String password) {
}