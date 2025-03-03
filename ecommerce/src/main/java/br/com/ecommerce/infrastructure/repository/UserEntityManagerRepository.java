package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.user.UserRepository;
import jakarta.persistence.Query;
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

    @Override
    public Optional<User> findBySubject(String subject) {
        var query = """
                SELECT user FROM User user
                JOIN FETCH user.group group
                JOIN FETCH group.roles
                WHERE UPPER(TRIM(subject)) = UPPER(TRIM(:subject))
                """;
        return Optional.ofNullable(entityManager.createQuery(query, User.class)
                .setParameter("subject", subject)
                .getSingleResult());
    }

    @Override
    public Optional<Long> getUserIdBySubject(String subject) {
        Query query = entityManager.createNativeQuery("""
                        SELECT id FROM {h-schema}tb_user
                        WHERE UPPER(TRIM(subject)) = UPPER(TRIM(:subject))
                        """)
                .setParameter("subject", subject);
        Object result = query.getSingleResult();
        if (result instanceof Long userId) {
            return Optional.of(userId);
        }
        return Optional.empty();
    }
}