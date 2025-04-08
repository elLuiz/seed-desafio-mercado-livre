package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.ProductCharacteristic;

public record ProductCharacteristicResponse(String property, String value) {
    public static ProductCharacteristicResponse convert(ProductCharacteristic productCharacteristic) {
        return new ProductCharacteristicResponse(productCharacteristic.getProperty(), productCharacteristic.getValue());
    }
}
