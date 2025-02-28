package br.com.ecommerce.infrastructure.session;

import br.com.ecommerce.domain.model.session.SessionUser;
import br.com.ecommerce.service.common.SessionUserService;
import br.com.ecommerce.service.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
class JwtSessionUserService implements SessionUserService {
    private final UserRepository userRepository;

    public JwtSessionUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SessionUser loadUserBySubject(String subject) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt jwt) {
            return new SessionUser(1L, jwt.getClaimAsString("full_name"), SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        }
        throw new IllegalStateException("invalid.session");
    }
}