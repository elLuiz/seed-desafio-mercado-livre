package br.com.ecommerce.domain.model.user;

import br.com.ecommerce.domain.common.StepValidator;
import br.com.ecommerce.domain.common.UserValidationConstants;
import br.com.ecommerce.domain.common.ValidationErrors;
import br.com.ecommerce.util.Either;
import br.com.ecommerce.util.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

class UserValidator implements StepValidator {
    public static final String FULL_NAME = "fullName";
    private final ValidationErrors errors = new ValidationErrors();

    public UserValidator hasValidEmail(String email) {
        if (StringUtils.isNullOrEmpty(email)) {
            errors.add("email", "login.must.not.be.null");
        } else if (!EmailValidator.getInstance().isValid(email)) {
            errors.add("email", "login.must.not.be.invalid.email");
        }
        return this;
    }

    public UserValidator hasValidName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            errors.add(FULL_NAME, "full.name.must.not.be.null");
        } else if (name.length() > 120) {
            errors.add(FULL_NAME, "full.name.must.not.surpass.120.characters");
        } else if (!name.matches(UserValidationConstants.NAME_EXPRESSION)) {
            errors.add(FULL_NAME, "name.must.not.violate.pattern");
        }
        return this;
    }

    public UserValidator hasValidPassword(String plainTextPassword) {
        if (StringUtils.isNullOrEmpty(plainTextPassword)) {
            errors.add("password", "password.must.not.be.null");
        } else if (!plainTextPassword.matches(UserValidationConstants.PASSWORD_EXPRESSION)) {
            errors.add("password", "password.must.not.violate.pattern");
        }
        return this;
    }

    @Override
    public Either<ValidationErrors, Boolean> evaluate() {
        if (errors.hasAnyError()) {
            return Either.error(errors);
        }
        return Either.correct(true);
    }
}