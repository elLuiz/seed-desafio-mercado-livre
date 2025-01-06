package br.com.ecommerce.application.user;

import br.com.ecommerce.application.user.dto.UserCreatedResponse;
import br.com.ecommerce.domain.command.CreateUserCommand;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.user.RegisterUserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/users")
public class CreateUserController {
    private final RegisterUserService registerUserService;

    public CreateUserController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> register(@Valid @RequestBody CreateUserCommand createUserCommand) {
        User user = registerUserService.register(createUserCommand);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri())
                .body(UserCreatedResponse.toResponse(user));
    }
}