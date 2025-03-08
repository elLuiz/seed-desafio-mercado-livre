package br.com.ecommerce.application.product;

import br.com.ecommerce.application.product.request.ProductQuestionRequest;
import br.com.ecommerce.application.product.response.ProductQuestionCreatedResponse;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import br.com.ecommerce.domain.model.product.command.RegisterQuestionCommand;
import br.com.ecommerce.domain.model.session.SessionUser;
import br.com.ecommerce.service.common.SessionUserService;
import br.com.ecommerce.service.product.RegisterQuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class RegisterProductQuestionController {
    private final RegisterQuestionService registerQuestionService;
    private final SessionUserService sessionUserService;

    public RegisterProductQuestionController(RegisterQuestionService registerQuestionService,
                                             SessionUserService sessionUserService) {
        this.registerQuestionService = registerQuestionService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping(value = "/{id}/questions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerQuestion(@AuthenticationPrincipal Jwt jwt,
                                                   @PathVariable("id") Long productId,
                                                   @RequestBody @Valid ProductQuestionRequest productQuestionRequest) {
        SessionUser sessionUser = sessionUserService.loadUserBySubject(jwt.getSubject());
        ProductQuestion productQuestion = registerQuestionService.registerQuestion(new RegisterQuestionCommand(sessionUser, productId, productQuestionRequest.question()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductQuestionCreatedResponse.toResponse(productQuestion));
    }
}