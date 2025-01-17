package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.common.validation.StepValidator;
import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.util.Either;
import br.com.ecommerce.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

class ProductValidator extends StepValidator {

    public ProductValidator checkValidDescription(String description) {
        if (StringUtils.isNullOrEmpty(description)) {
            validationErrors.add("description", ProductValidationCodes.DESCRIPTION_MUST_NOT_BE_EMPTY);
        } else if (StringUtils.greaterThan(description, 1_000)) {
            validationErrors.add("description", ProductValidationCodes.DESCRIPTION_MUST_NOT_SURPASS_1000_CHARACTERS);
        }
        return this;
    }

    public ProductValidator checkValidPrice(BigDecimal price) {
        if (price == null) {
            validationErrors.add("price", ProductValidationCodes.PRICE_MUST_NOT_BE_NULL);
        } else if (price.compareTo(BigDecimal.ZERO) < 0 || price.compareTo(BigDecimal.valueOf(2)) < 0) {
            validationErrors.add("price", ProductValidationCodes.INVALID_PRICE);
        }
        return this;
    }

    public ProductValidator checkValidName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            validationErrors.add("name", ProductValidationCodes.PRODUCT_NAME_MUST_NOT_BE_EMPTY);
        } else if (StringUtils.greaterThan(name, 255)) {
            validationErrors.add("name", ProductValidationCodes.PRODUCT_NAME_MUST_NOT_SURPASS_255_CHARACTERS);
        }
        return this;
    }

    public ProductValidator checkValidStockQuantity(Integer stockQuantity) {
        if (stockQuantity == null) {
            validationErrors.add("stockQuantity", ProductValidationCodes.QUANTITY_MUST_NOT_BE_EMPTY);
        } else if (stockQuantity <= 0) {
            validationErrors.add("stockQuantity", ProductValidationCodes.QUANTITY_MUST_BE_POSITIVE);
        }
        return this;
    }

    public ProductValidator checkValidCategory(Category category) {
        if (category == null) {
            validationErrors.add("category", ProductValidationCodes.CATEGORY_MUST_NOT_BE_NULL);
        }
        return this;
    }

    public ProductValidator checkCharacteristics(List<ProductCharacteristic> productCharacteristics) {
        Either<ValidationErrors, Boolean> evaluate = new ProductCharacteristicsValidator()
                .checkCharacteristics(productCharacteristics)
                .evaluate();
        if (!evaluate.valid()) {
            validationErrors.append(evaluate.getError());
        }
        return this;
    }

    public ProductValidator checkOwner(User owner) {
        if (owner == null) {
            validationErrors.add("owner", "owner.must.not.be.null");
        }
        return this;
    }
}