package br.com.ecommerce.application.product;

import br.com.ecommerce.application.product.request.ReviewProductRequest;
import br.com.ecommerce.application.product.response.ProductReviewedResponse;
import br.com.ecommerce.domain.model.product.ProductReview;
import br.com.ecommerce.domain.model.product.command.ReviewProductCommand;
import br.com.ecommerce.service.common.SessionUserService;
import br.com.ecommerce.service.product.ReviewProductService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/api/v1/products")
@RestController
public class ProductReviewController {
    private final ReviewProductService reviewProductService;
    private final SessionUserService sessionUserService;

    public ProductReviewController(ReviewProductService reviewProductService, SessionUserService sessionUserService) {
        this.reviewProductService = reviewProductService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping(value = "/{id}/reviews", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reviewProduct(@PathVariable("id") Long productId,
                                                @AuthenticationPrincipal Jwt jwt,
                                                @RequestBody @Valid ReviewProductRequest reviewProductRequest) {
        ProductReview productReview = this.reviewProductService.reviewProduct(new ReviewProductCommand(productId, reviewProductRequest.rating(), reviewProductRequest.title(), reviewProductRequest.description(), sessionUserService.loadUserBySubject(jwt.getSubject())));
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(productReview.getId()).toUri())
                .body(ProductReviewedResponse.toResponse(productReview));
    }
}