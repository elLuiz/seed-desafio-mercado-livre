package br.com.ecommerce.domain.common.validation;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ValidationErrors {
    private Set<ValidationError> errors;

    public void add(String field, String code) {
        if (this.errors == null) {
            this.errors = new HashSet<>();
        }
        this.errors.add(new ValidationError(field, code));
    }

    public boolean hasAnyError() {
        return errors != null && !errors.isEmpty();
    }

    public static ValidationErrors single(String field, String code) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.add(field, code);
        return validationErrors;
    }

    public void append(ValidationErrors validationErrors) {
        if (validationErrors.hasAnyError()) {
            if (this.errors == null) {
                this.errors = new HashSet<>();
            }
            errors.addAll(validationErrors.getErrors());
        }
    }
}