package br.com.ecommerce.domain.model.product;

public class ProductValidationCodes {
    private ProductValidationCodes() {}
    public static final String PROPERTY_MUST_NOT_BE_EMPTY = "property.must.not.be.empty";
    public static final String PROPERTY_MUST_NOT_SURPASS_100_CHARACTERS = "property.must.not.surpass.100.characters";
    public static final String PROPERTY_VALUE_MUST_NOT_BE_EMPTY = "property.value.must.not.be.empty";
    public static final String PROPERTY_VALUE_MUST_NOT_SURPASS_100_CHARACTERS = "property.value.must.not.surpass.100.characters";
    public static final String PRODUCT_NAME_MUST_NOT_BE_EMPTY = "product.name.must.not.be.empty";
    public static final String PRODUCT_NAME_MUST_NOT_SURPASS_255_CHARACTERS = "product.name.must.not.surpass.255.characters";
    public static final String PRICE_MUST_NOT_BE_NULL = "price.must.not.be.null";
    public static final String INVALID_PRICE = "invalid.price";
    public static final String QUANTITY_MUST_NOT_BE_EMPTY = "quantity.must.not.be.empty";
    public static final String QUANTITY_MUST_BE_POSITIVE = "quantity.must.be.positive";
    public static final String CHARACTERISTICS_MUST_BE_SPECIFIED = "characteristics.must.be.specified";
    public static final String DESCRIPTION_MUST_NOT_BE_EMPTY = "description.must.not.be.empty";
    public static final String DESCRIPTION_MUST_NOT_SURPASS_1000_CHARACTERS = "description.must.not.surpass.1000.characters";
    public static final String CATEGORY_MUST_NOT_BE_NULL = "category.must.not.be.null";
}
