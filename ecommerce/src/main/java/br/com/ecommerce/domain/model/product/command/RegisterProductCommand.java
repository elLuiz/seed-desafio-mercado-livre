package br.com.ecommerce.domain.model.product.command;

import br.com.ecommerce.domain.model.product.ProductCharacteristic;
import br.com.ecommerce.domain.model.product.ProductValidationCodes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record RegisterProductCommand(@NotBlank(message = ProductValidationCodes.PRODUCT_NAME_MUST_NOT_BE_EMPTY)
                                     @Size(max = 255, message = ProductValidationCodes.PRODUCT_NAME_MUST_NOT_SURPASS_255_CHARACTERS) String name,
                                     @NotNull(message = ProductValidationCodes.PRICE_MUST_NOT_BE_NULL)
                                     @DecimalMin(value = "2.0", message = ProductValidationCodes.INVALID_PRICE)
                                     @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = ProductValidationCodes.INVALID_PRICE) BigDecimal price,
                                     @NotNull(message = ProductValidationCodes.QUANTITY_MUST_NOT_BE_EMPTY)
                                     @Positive(message = ProductValidationCodes.QUANTITY_MUST_BE_POSITIVE) Integer stockQuantity,
                                     @NotEmpty(message = ProductValidationCodes.CHARACTERISTICS_MUST_BE_SPECIFIED)
                                     @NotNull(message = ProductValidationCodes.CHARACTERISTICS_MUST_BE_SPECIFIED) List<@Valid ProductCharacteristics> characteristics,
                                     @NotBlank(message = ProductValidationCodes.DESCRIPTION_MUST_NOT_BE_EMPTY)
                                     @Size(max = 1_000, message = ProductValidationCodes.DESCRIPTION_MUST_NOT_SURPASS_1000_CHARACTERS) String description,
                                     @NotNull(message = ProductValidationCodes.CATEGORY_MUST_NOT_BE_NULL) Long categoryId) {

    public List<ProductCharacteristic> convertCharacteristics() {
        if (characteristics == null) {
            return List.of();
        }
        return characteristics.stream()
                .map(productCharacteristics -> new ProductCharacteristic(productCharacteristics.property(), productCharacteristics.value()))
                .toList();
    }
}