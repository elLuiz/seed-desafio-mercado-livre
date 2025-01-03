package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.model.user.User;

import java.util.Optional;

public interface UserRepository {
    void add(User user);
    Optional<User> findByLogin(String login);
}