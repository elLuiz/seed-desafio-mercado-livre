package br.com.ecommerce.application.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class WithMockJwtFactory implements WithSecurityContextFactory<WithMockJwt> {
    private final JwtFactory jwtFactory;

    public WithMockJwtFactory(JwtFactory jwtFactory) {
        this.jwtFactory = jwtFactory;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockJwt annotation) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Jwt jwt = jwtFactory.createJwt(annotation.expired(), annotation.subject(), annotation.roles());
        Authentication authentication = new JwtAuthenticationToken(jwt, AuthorityUtils.createAuthorityList(annotation.roles()));
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}