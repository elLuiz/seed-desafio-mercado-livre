package br.com.ecommerce.domain.exception;

import br.com.ecommerce.domain.common.ValidationErrors;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final transient ValidationErrors errors;

    public ValidationException(ValidationErrors errors) {
        this.errors = errors;
    }
}