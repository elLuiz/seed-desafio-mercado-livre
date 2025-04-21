package br.com.ecommerce.domain.model.product;

public enum ProductStockStatus {
    AVAILABLE,
    DEDUCTED,
    OUT_OF_STOCK;

    public static ProductStockStatus convert(int amount) {
        if (amount <= 0) {
            return OUT_OF_STOCK;
        }
        return AVAILABLE;
    }
}