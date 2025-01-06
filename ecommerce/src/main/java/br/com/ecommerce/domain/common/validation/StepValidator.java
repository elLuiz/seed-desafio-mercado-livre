package br.com.ecommerce.domain.common.validation;

import br.com.ecommerce.util.Either;

public interface StepValidator {
    Either<ValidationErrors, Boolean> evaluate();
}