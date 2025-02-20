package br.com.ecommerce.application.product;

import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.command.RegisterProductCommand;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.product.RegisterProductService;
import br.com.ecommerce.service.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/products")
public class RegisterProductController {
    private final RegisterProductService registerProductService;
    private final UserRepository userRepository;


    public RegisterProductController(RegisterProductService registerProductService, UserRepository userRepository) {
        this.registerProductService = registerProductService;
        this.userRepository = userRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterProductCommand registerProductCommand, @AuthenticationPrincipal Jwt jwt) {
        User owner = this.userRepository.findBySubject(jwt.getSubject()).orElseThrow();
        Product product = registerProductService.register(registerProductCommand, owner);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(product.getId())
                        .toUri()
                )
                .body(ProductCreatedResponse.toResponse(product));
    }
}