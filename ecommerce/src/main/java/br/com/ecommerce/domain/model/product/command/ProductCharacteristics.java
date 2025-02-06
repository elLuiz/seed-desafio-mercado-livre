package br.com.ecommerce.domain.model.product.command;

import br.com.ecommerce.domain.model.product.ProductValidationCodes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductCharacteristics(@NotBlank(message = ProductValidationCodes.PROPERTY_MUST_NOT_BE_EMPTY)
                                     @Size(max = 100, message = ProductValidationCodes.PROPERTY_MUST_NOT_SURPASS_100_CHARACTERS) String property,
                                     @NotBlank(message = ProductValidationCodes.PROPERTY_VALUE_MUST_NOT_BE_EMPTY)
                                     @Size(max = 100, message = ProductValidationCodes.PROPERTY_VALUE_MUST_NOT_SURPASS_100_CHARACTERS) String value) {
    }
