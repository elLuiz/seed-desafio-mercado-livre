package br.com.ecommerce.service.common;

import br.com.ecommerce.domain.model.session.SessionUser;

public interface SessionUserService {
    SessionUser loadUserBySubject(String subject);
}
