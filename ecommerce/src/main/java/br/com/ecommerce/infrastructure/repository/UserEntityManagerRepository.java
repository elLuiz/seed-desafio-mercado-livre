package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.service.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserEntityManagerRepository implements UserRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        var query = entityManager.createQuery("SELECT user FROM User user " +
                "WHERE UPPER(TRIM(login))=:UPPER(TRIM(:login))", User.class);
        query.setParameter("login", login);
        return Optional.ofNullable(query.getSingleResult());
    }
}