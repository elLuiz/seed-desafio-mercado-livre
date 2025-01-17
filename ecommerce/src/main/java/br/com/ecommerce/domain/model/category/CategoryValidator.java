package br.com.ecommerce.domain.model.category;

import br.com.ecommerce.domain.common.validation.StepValidator;
import br.com.ecommerce.util.StringUtils;

class CategoryValidator extends StepValidator {
    public StepValidator validateName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            validationErrors.add("name", "category.must.not.be.empty");
        } else if (name.length() > 255) {
            validationErrors.add("name", "category.surpasses.limit");
        }
        return this;
    }
}
