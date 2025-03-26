package br.com.ecommerce.application.user;

import br.com.ecommerce.application.common.ResourceCreatedResponse;
import br.com.ecommerce.application.user.dto.UserCreatedResponse;
import br.com.ecommerce.domain.command.CreateAccountCommand;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.user.RegisterAccountService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
public class CreateAccountController {
    private final RegisterAccountService registerAccountService;

    public CreateAccountController(RegisterAccountService registerAccountService) {
        this.registerAccountService = registerAccountService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> register(@Valid @RequestBody CreateAccountCommand createAccountCommand) {
        User user = registerAccountService.register(createAccountCommand);
        return ResourceCreatedResponse.created(UserCreatedResponse.toResponse(user), "/{id}", user.getId());
    }
}