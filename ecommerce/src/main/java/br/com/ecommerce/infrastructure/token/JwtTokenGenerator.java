package br.com.ecommerce.infrastructure.token;

import br.com.ecommerce.application.login.TokenGenerator;
import br.com.ecommerce.application.login.dto.LoginResponse;
import br.com.ecommerce.domain.model.permission.role.Role;
import br.com.ecommerce.domain.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class JwtTokenGenerator implements TokenGenerator {
    private final String issuer;
    private final JwtEncoder jwtEncoder;

    public JwtTokenGenerator(@Value("${jwt.issuer}") String issuer,
                             JwtEncoder jwtEncoder) {
        this.issuer = issuer;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public LoginResponse generateToken(User user) {
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .expiresAt(Instant.now().plusSeconds(3600))
                .issuedAt(Instant.now())
                .subject(user.getSubject())
                .claim("full_name", user.getFullName())
                .claim("roles", getRoles(user))
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        return new LoginResponse(token, 3600L);
    }

    private static Set<String> getRoles(User user) {
       return user.getAssignedRoles()
               .stream()
               .map(Role::getRoleAcronym)
               .collect(Collectors.toSet());
    }
}
