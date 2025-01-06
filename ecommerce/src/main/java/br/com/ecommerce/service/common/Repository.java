package br.com.ecommerce.service.common;

import java.util.Optional;

public interface Repository<E> {
    Optional<E> findById(Long id);
    void add(E entity);
}
