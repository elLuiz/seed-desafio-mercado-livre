package br.com.ecommerce.domain.common;

import br.com.ecommerce.util.Either;

public interface StepValidator {
    Either<ValidationErrors, Boolean> evaluate();
}