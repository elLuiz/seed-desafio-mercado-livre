package br.com.ecommerce.domain.common.validation;

import br.com.ecommerce.util.Either;

public abstract class StepValidator {
    protected final ValidationErrors validationErrors = new ValidationErrors();

    public Either<ValidationErrors, Boolean> evaluate() {
        return validationErrors.hasAnyError() ? Either.error(validationErrors) : Either.correct(true);
    }
}