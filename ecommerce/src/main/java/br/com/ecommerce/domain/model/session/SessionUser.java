package br.com.ecommerce.domain.model.session;

import java.util.Set;

public record SessionUser(Long id, String fullName, Set<String> roles) {}