package br.com.ecommerce.service.user;

import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.common.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User> {
    Optional<User> findByLogin(String login);
}