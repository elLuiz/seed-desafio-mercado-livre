package br.com.ecommerce.domain.model.order;

import br.com.ecommerce.domain.common.validation.StepValidator;
import br.com.ecommerce.domain.model.product.Product;

class OrderValidator extends StepValidator {
    public OrderValidator checkCustomer(Long customerId) {
        if (customerId == null) {
            validationErrors.add("customerId", "customer.must.not.be.null");
        }
        return this;
    }

    public OrderValidator checkPaymentMethod(PaymentGateway paymentGateway) {
        if (paymentGateway == null) {
            validationErrors.add("paymentGateway", "payment.gateway.must.be.selected");
        }
        return this;
    }

    public OrderValidator checkQuantity(Integer quantity) {
        if (quantity == null) {
            validationErrors.add("quantity", "quantity.must.not.be.null");
        } else if (quantity < 1) {
            validationErrors.add("quantity", "quantity.must.be.positive");
        }
        return this;
    }

    public OrderValidator checkProduct(Product product) {
        if (product == null || product.getId() == null) {
            validationErrors.add("product", "product.not.found");
        } else if (product.getPrice() == null) {
            validationErrors.add("price", "product.with.invalid.price");
        }
        return this;
    }
}