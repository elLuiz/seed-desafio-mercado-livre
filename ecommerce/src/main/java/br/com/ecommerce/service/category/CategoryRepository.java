package br.com.ecommerce.service.category;

import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.service.common.Repository;

import java.util.Optional;

public interface CategoryRepository extends Repository<Category> {
    Optional<Category> findByName(String name);
}