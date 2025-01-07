package br.com.ecommerce.application.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class WithMockJwtFactory implements WithSecurityContextFactory<WithMockJwt> {
    private final JwtEncoder jwtEncoder;
    private final String issuer;

    public WithMockJwtFactory(JwtEncoder jwtEncoder,
                              @Value("${jwt.issuer}") String issuer) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockJwt annotation) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Instant expiresAt = Instant.now().plusSeconds(3_600);
        Instant issuedAt = Instant.now();
        if (annotation.expired()) {
            issuedAt = Instant.now().minusSeconds(600);
            expiresAt = Instant.now().minusSeconds(500);
        }
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .expiresAt(expiresAt)
                .issuedAt(issuedAt)
                .subject(annotation.subject())
                .claim("roles", annotation.roles())
                .build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
        Authentication authentication = new JwtAuthenticationToken(jwt, AuthorityUtils.createAuthorityList(annotation.roles()));
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
