package br.com.ecommerce.application.product.response;

import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductReview;
import br.com.ecommerce.domain.model.product.ProductReviewAuthor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

class ReviewInformationTest {
    @Test
    void shouldReturnProductReviewsInformation() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId())
                .thenReturn(1L);
        List<ProductReview> productReviews = List.of(new ProductReview(5, "Good", "Very good", new ProductReviewAuthor(1L), product),
                new ProductReview(1, "Bad", "I don't know; it simply does not work well.", new ProductReviewAuthor(2L), product),
                new ProductReview(2, "Could be better", "Empty", new ProductReviewAuthor(3L), product));

        ReviewInformation reviewInformation = ReviewInformation.convert(productReviews);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(BigDecimal.valueOf(2.7), reviewInformation.getAverage());
            Assertions.assertEquals(3L, reviewInformation.getAmount());
            Assertions.assertEquals(3, reviewInformation.getReviews().size());
        });
    }

    @Test
    void shouldReturnEmptyWhenProductHasNotBeenReviewedYet() {
        ReviewInformation reviewInformation = ReviewInformation.convert(null);

        Assertions.assertEquals(ReviewInformation.EMPTY_REVIEW,  reviewInformation);
    }
}