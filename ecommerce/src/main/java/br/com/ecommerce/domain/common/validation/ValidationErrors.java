package br.com.ecommerce.domain.common.validation;

import java.util.HashSet;
import java.util.Set;

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

    public Set<ValidationError> getErrors() {
        return errors;
    }

    public static ValidationErrors single(String field, String code) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.add(field, code);
        return validationErrors;
    }
}
