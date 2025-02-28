package br.com.ecommerce.domain.model.product;

import br.com.ecommerce.domain.common.validation.StepValidator;
import br.com.ecommerce.util.StringUtils;

public class ProductReviewValidator extends StepValidator {
    private static final String RATING = "rating";

    public ProductReviewValidator checkTitle(String title) {
        if (StringUtils.isNullOrEmpty(title)) {
            validationErrors.add("title", "title.must.not.be.empty");
        } else if (StringUtils.greaterThan(title, 255)) {
            validationErrors.add("title", "title.must.not.exceed.255.characters");
        }
        return this;
    }

    public ProductReviewValidator checkDescription(String description) {
        if (StringUtils.isNullOrEmpty(description)) {
            validationErrors.add("description", "description.must.not.be.empty");
        } else if (StringUtils.greaterThan(description, 500)) {
            validationErrors.add("description", "description.must.not.exceed.500.characters");
        }
        return this;
    }

    public ProductReviewValidator checkAuthor(ProductReviewAuthor author) {
        if (author == null) {
            validationErrors.add("author", "author.must.not.be.null");
        }
        return this;
    }

    public StepValidator checkProduct(Product product) {
        if (product == null) {
            validationErrors.add("product", "product.must.not.be.null");
        }
        return this;
    }

    public ProductReviewValidator checkRating(Integer rating) {
        if (rating == null) {
            validationErrors.add(RATING, "rating.must.not.be.null");
        } else if (rating < 1) {
            validationErrors.add(RATING, "rating.minimum.must.not.be.lesser.than.one");
        } else if (rating > 5) {
            validationErrors.add(RATING, "rating.maximum.must.not.be.greater.than.five");
        }
        return this;
    }
}