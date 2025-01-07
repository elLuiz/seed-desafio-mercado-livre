package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserEntityManagerRepository extends GenericRepository<User> implements UserRepository {
    UserEntityManagerRepository() {
        super(User.class);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        var query = """
                SELECT user FROM User user
                JOIN FETCH user.group group
                JOIN FETCH group.roles
                WHERE UPPER(TRIM(login)) = UPPER(TRIM(:login))
                """;
        return Optional.ofNullable(entityManager.createQuery(query, User.class)
                .setParameter("login", login)
                .getSingleResult());
    }
}