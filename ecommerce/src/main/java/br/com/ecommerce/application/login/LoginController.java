package br.com.ecommerce.application.login;

import br.com.ecommerce.application.login.dto.LoginRequest;
import br.com.ecommerce.application.login.dto.LoginResponse;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.user.CheckUserCredentialsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/login")
public class LoginController {
    private final CheckUserCredentialsService checkUserCredentialsService;
    private final TokenGenerator tokenGenerator;

    public LoginController(CheckUserCredentialsService checkUserCredentialsService,
                           TokenGenerator tokenGenerator) {
        this.checkUserCredentialsService = checkUserCredentialsService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> handleLogin(@RequestBody @Valid LoginRequest loginRequest) {
        User user = checkUserCredentialsService.loadByLoginAndPassword(loginRequest.login(), loginRequest.password())
                .orElseThrow(ValidationException::new);
        return ResponseEntity.ok(tokenGenerator.generateToken(user));
    }
}