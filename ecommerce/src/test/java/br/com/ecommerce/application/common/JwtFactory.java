package br.com.ecommerce.application.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtFactory {
    private final String issuer;
    private final JwtEncoder jwtEncoder;

    public JwtFactory(@Value("${jwt.issuer}") String issuer,
                      JwtEncoder jwtEncoder) {
        this.issuer = issuer;
        this.jwtEncoder = jwtEncoder;
    }

    public Jwt createJwt(boolean expired, String subject, String[] roles) {
        Instant expiresAt = Instant.now().plusSeconds(3_600);
        Instant issuedAt = Instant.now();
        if (expired) {
            issuedAt = Instant.now().minusSeconds(600);
            expiresAt = Instant.now().minusSeconds(500);
        }
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .expiresAt(expiresAt)
                .issuedAt(issuedAt)
                .subject(subject)
                .claim("roles", roles)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
    }
}