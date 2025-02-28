package br.com.ecommerce.domain.common.validation;

import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.util.Either;

public abstract class StepValidator {
    protected final ValidationErrors validationErrors = new ValidationErrors();

    public static void assertTrueOrThrowValidationException(boolean condition, String field, String code) {
        if (!condition) {
            throw new ValidationException(ValidationErrors.single(field, code));
        }
    }

    public Either<ValidationErrors, Boolean> evaluate() {
        return validationErrors.hasAnyError() ? Either.error(validationErrors) : Either.correct(true);
    }
}