package br.com.ecommerce.application.login;

import br.com.ecommerce.application.login.dto.LoginRequest;
import br.com.ecommerce.application.login.dto.LoginResponse;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.user.CheckUserCredentialsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth/login")
public class LoginController {
    private final JwtEncoder jwtEncoder;
    private final CheckUserCredentialsService checkUserCredentialsService;
    private final String issuer;

    public LoginController(JwtEncoder jwtEncoder,
                           CheckUserCredentialsService checkUserCredentialsService,
                           @Value("${jwt.issuer}") String issuer) {
        this.jwtEncoder = jwtEncoder;
        this.checkUserCredentialsService = checkUserCredentialsService;
        this.issuer = issuer;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> handleLogin(@RequestBody @Valid LoginRequest loginRequest) {
        User user = checkUserCredentialsService.loadByLoginAndPassword(loginRequest.login(), loginRequest.password())
                .orElseThrow(ValidationException::new);
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .expiresAt(Instant.now().plusSeconds(3600))
                .issuedAt(Instant.now())
                .subject(user.getSubject())
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        return ResponseEntity.ok(new LoginResponse(token, 3600L));
    }
}