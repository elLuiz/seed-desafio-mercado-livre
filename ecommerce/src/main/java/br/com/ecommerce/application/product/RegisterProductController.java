package br.com.ecommerce.application.product;

import br.com.ecommerce.application.common.ResourceCreatedResponse;
import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.command.RegisterProductCommand;
import br.com.ecommerce.domain.model.session.SessionUser;
import br.com.ecommerce.service.common.SessionUserService;
import br.com.ecommerce.service.product.RegisterProductService;
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

@RestController
@RequestMapping("/api/v1/products")
public class RegisterProductController {
    private final RegisterProductService registerProductService;
    private final SessionUserService sessionUserService;


    public RegisterProductController(RegisterProductService registerProductService, SessionUserService sessionUserService) {
        this.registerProductService = registerProductService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterProductCommand registerProductCommand, @AuthenticationPrincipal Jwt jwt) {
        SessionUser owner = this.sessionUserService.loadUserBySubject(jwt.getSubject());
        Product product = registerProductService.register(registerProductCommand, owner);
        return ResourceCreatedResponse.created(ProductCreatedResponse.toResponse(product), "/{id}", product.getId());
    }
}