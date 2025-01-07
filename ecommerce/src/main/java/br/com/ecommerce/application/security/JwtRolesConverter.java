package br.com.ecommerce.application.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtRolesConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        if (!source.hasClaim("roles")) {
            return new JwtAuthenticationToken(source, List.of());
        }
        Set<SimpleGrantedAuthority> roles = getRoles(source);
        return new JwtAuthenticationToken(source, roles);
    }

    private Set<SimpleGrantedAuthority> getRoles(Jwt source) {
        Collection<String> roles = source.getClaim("roles");
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
