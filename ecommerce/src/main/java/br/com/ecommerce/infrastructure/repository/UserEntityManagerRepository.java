package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.user.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
class UserEntityManagerRepository extends GenericRepository<User> implements UserRepository {
    UserEntityManagerRepository() {
        super(User.class);
    }
}