package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.common.validation.StepValidator;

public class ProductMediaValidator extends StepValidator {
    ProductMediaValidator checkFileName(String filename) {
        if (filename == null || filename.isBlank()) {
            validationErrors.add("filename", "filename.must.not.be.null");
        } else if (filename.length() > 255) {
            validationErrors.add("filename", "filename.must.not.exceed.255.characters");
        }
        return this;
    }

    ProductMediaValidator checkURL(String url) {
        if (url == null || url.isBlank()) {
            validationErrors.add("url", "url.must.not.be.null");
        } else if (url.length() > 500) {
            validationErrors.add("url", "url.must.not.exceed.500.characters");
        }
        return this;
    }
}