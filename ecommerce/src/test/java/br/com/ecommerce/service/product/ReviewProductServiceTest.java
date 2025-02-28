package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.exception.DomainException;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductReview;
import br.com.ecommerce.domain.model.product.ProductReviewAuthor;
import br.com.ecommerce.domain.model.product.command.ReviewProductCommand;
import br.com.ecommerce.domain.model.session.SessionUser;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ReviewProductServiceTest {
    @Mock
    ProductRepository productRepository;
    ReviewProductService reviewProductService;
    SessionUser sessionUser;

    @BeforeEach
    void setUp() {
        this.reviewProductService = new ReviewProductService(productRepository);
        sessionUser = new SessionUser(1L, "JUNIT TEST", Set.of("GROUP_CREATE_ROLE", "GROUP_READ_ROLE"));
    }

    @Test
    void shouldNotReviewProductWhenItDoesNotExist() {
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        ReviewProductCommand reviewProductCommand = new ReviewProductCommand(1L, 4, "title", "description", sessionUser);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            reviewProductService.reviewProduct(reviewProductCommand);
        });
    }

    @Test
    void shouldNotReviewProductWhenAtLeastOnePropertyIsInvalid() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ReviewProductCommand reviewProductCommand = new ReviewProductCommand(1L, 6, "title", "description", sessionUser);
        Assertions.assertThrows(ValidationException.class, () -> reviewProductService.reviewProduct(reviewProductCommand));
    }

    @Test
    void shouldNotReviewProductWhenAuthorHasAlreadyReviewedProduct() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        Mockito.when(productRepository.hasUserReviewedProduct(product.getId(), sessionUser.id()))
                .thenReturn(true);

        ReviewProductCommand reviewProductCommand = new ReviewProductCommand(1L, 6, "title", "description", sessionUser);
        Assertions.assertThrows(DomainException.class, () -> reviewProductService.reviewProduct(reviewProductCommand));
    }

    @Test
    void shouldAddReviewToProduct() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).addReview(new ProductReview(5, "title", "description", new ProductReviewAuthor(1L), product));
        ReviewProductCommand reviewProductCommand = new ReviewProductCommand(1L, 5, "title", "description", sessionUser);
        reviewProductService.reviewProduct(reviewProductCommand);
        Mockito.verify(productRepository, Mockito.times(1)).addReview(new ProductReview(5, "title", "description", new ProductReviewAuthor(1L), product));
    }
}