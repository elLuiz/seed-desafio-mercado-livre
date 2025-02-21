package br.com.ecommerce.application.product;

import br.com.ecommerce.domain.model.product.command.ReviewProductCommand;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RestController
public class ProductReviewController {
    @PostMapping(value = "/{id}/reviews", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reviewProduct(@PathVariable("id") Long productId, @RequestBody @Valid ReviewProductCommand reviewProductCommand) {
        return ResponseEntity.noContent().build();
    }
}