package br.com.ecommerce.domain.model.category;

import br.com.ecommerce.domain.common.validation.StepValidator;
import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.util.Either;
import br.com.ecommerce.util.StringUtils;

class CategoryValidator implements StepValidator {
    private final ValidationErrors validationErrors = new ValidationErrors();

    public StepValidator validateName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            validationErrors.add("name", "category.must.not.be.empty");
        } else if (name.length() > 255) {
            validationErrors.add("name", "category.surpasses.limit");
        }
        return this;
    }


    @Override
    public Either<ValidationErrors, Boolean> evaluate() {
        if (validationErrors.hasAnyError()) {
            return Either.error(validationErrors);
        }
        return Either.correct(true);
    }
}
