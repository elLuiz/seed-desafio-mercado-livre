package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.common.validation.StepValidator;
import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.util.StringUtils;

import java.util.List;

public class ProductCharacteristicsValidator extends StepValidator {

    public StepValidator checkCharacteristics(List<ProductCharacteristic> productCharacteristics) {
        if (productCharacteristics == null || productCharacteristics.isEmpty()) {
            validationErrors.add("productCharacteristics", ProductValidationCodes.CHARACTERISTICS_MUST_BE_SPECIFIED);
        } else {
            for (int i = 0; i < productCharacteristics.size(); i++) {
                ProductCharacteristic productCharacteristic = productCharacteristics.get(i);
                checkProperty(validationErrors, productCharacteristic, i);
                checkValue(validationErrors, productCharacteristic, i);
            }
        }
        return this;
    }

    private void checkValue(ValidationErrors validationErrors, ProductCharacteristic productCharacteristic, int index) {
        if (StringUtils.isNullOrEmpty(productCharacteristic.getValue())) {
            validationErrors.add("productCharacteristics[%d].value".formatted(index), ProductValidationCodes.PROPERTY_VALUE_MUST_NOT_BE_EMPTY);
        } else if (StringUtils.greaterThan(productCharacteristic.getProperty(), 100)) {
            validationErrors.add("productCharacteristics[%d].value", ProductValidationCodes.PROPERTY_VALUE_MUST_NOT_SURPASS_100_CHARACTERS);
        }
    }

    private void checkProperty(ValidationErrors validationErrors, ProductCharacteristic productCharacteristic, int i) {
        if (StringUtils.isNullOrEmpty(productCharacteristic.getProperty())) {
            validationErrors.add("productCharacteristics[%d].property".formatted(i), ProductValidationCodes.PROPERTY_MUST_NOT_BE_EMPTY);
        } else if (StringUtils.greaterThan(productCharacteristic.getProperty(), 100)) {
            validationErrors.add("productCharacteristics[%d].property", ProductValidationCodes.PROPERTY_MUST_NOT_SURPASS_100_CHARACTERS);
        }
    }
}