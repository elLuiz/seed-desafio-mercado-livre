package br.com.ecommerce.domain.model.user;

import br.com.ecommerce.domain.common.validation.StepValidator;
import br.com.ecommerce.domain.model.permission.group.Group;
import br.com.ecommerce.util.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

class UserValidator extends StepValidator {
    public static final String FULL_NAME = "fullName";
    public UserValidator hasValidEmail(String email) {
        if (StringUtils.isNullOrEmpty(email)) {
            validationErrors.add("email", "login.must.not.be.null");
        } else if (!EmailValidator.getInstance().isValid(email)) {
            validationErrors.add("email", "login.must.not.be.invalid.email");
        }
        return this;
    }

    public UserValidator hasValidName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            validationErrors.add(FULL_NAME, "full.name.must.not.be.null");
        } else if (name.length() > 120) {
            validationErrors.add(FULL_NAME, "full.name.must.not.surpass.120.characters");
        } else if (!name.matches(UserValidationConstants.NAME_EXPRESSION)) {
            validationErrors.add(FULL_NAME, "name.must.not.violate.pattern");
        }
        return this;
    }

    public UserValidator hasValidPassword(String plainTextPassword) {
        if (StringUtils.isNullOrEmpty(plainTextPassword)) {
            validationErrors.add("password", "password.must.not.be.null");
        } else if (!plainTextPassword.matches(UserValidationConstants.PASSWORD_EXPRESSION)) {
            validationErrors.add("password", "password.must.not.violate.pattern");
        }
        return this;
    }

    public UserValidator hasValidGroup(Group group) {
        if (group == null) {
            validationErrors.add("group", "group.cannot.be.null");
        }
        return this;
    }
}