package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.GenericEntity;
import br.com.ecommerce.service.common.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

public class GenericRepository<E extends GenericEntity> implements Repository<E> {
    @PersistenceContext
    protected EntityManager entityManager;
    private Class<E> entityClass;

    protected GenericRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void add(E entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
    }

    @Override
    public Optional<E> findById(Long id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }
}
