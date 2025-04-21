package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.Money;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductMedia;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import br.com.ecommerce.domain.model.product.ProductReview;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Data
public class ProductResponse {
    private List<String> mediaLinks;
    private String name;
    private BigDecimal price;
    private List<ProductCharacteristicResponse> characteristics;
    private String description;
    private ReviewInformation review;
    private List<ProductQuestionResponse> questions;

    public static ProductResponse convert(Product product, List<ProductReview> reviews, List<ProductQuestion> questions) {
        ProductResponse productResponse = new ProductResponse();
        convertMedias(product, productResponse);
        productResponse.setName(product.getProductName());
        convertPrice(product, productResponse);
        convertCharacteristics(product, productResponse);
        productResponse.setDescription(product.getDescription());
        productResponse.setReview(ReviewInformation.convert(reviews));
        convertQuestions(questions, productResponse);
        return productResponse;
    }

    private static void convertMedias(Product product, ProductResponse productResponse) {
        if (product.getMedias() != null) {
            productResponse.setMediaLinks(product.getMedias().stream().map(ProductMedia::getUrl).toList());
        }
    }

    private static void convertPrice(Product product, ProductResponse productResponse) {
        productResponse.setPrice(Optional.of(product.getPrice()).map(Money::getValue).map(value -> value.setScale(2, RoundingMode.HALF_UP)).orElse(null));
    }

    private static void convertCharacteristics(Product product, ProductResponse productResponse) {
        if (product.getProductCharacteristics() != null) {
            productResponse.setCharacteristics(product.getProductCharacteristics().stream().map(ProductCharacteristicResponse::convert).toList());
        }
    }

    private static void convertQuestions(List<ProductQuestion> questions, ProductResponse productResponse) {
        if (questions != null) {
            productResponse.setQuestions(questions.stream().map(ProductQuestionResponse::convert).toList());
        }
    }
}