package br.com.ecommerce.application.login.dto;

import br.com.ecommerce.domain.model.user.UserValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(@NotBlank(message = "login.must.not.be.null")
                           @Email(message = "login.must.not.be.invalid.email") String login,
                           @NotBlank(message = "password.must.not.be.null")
                           @Pattern(regexp = UserValidationConstants.PASSWORD_EXPRESSION, message = "password.must.not.violate.pattern") String password) {
}