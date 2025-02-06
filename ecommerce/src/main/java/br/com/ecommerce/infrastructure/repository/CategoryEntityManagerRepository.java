package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.service.category.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class CategoryEntityManagerRepository extends GenericRepository<Category> implements CategoryRepository {
    CategoryEntityManagerRepository() {
        super(Category.class);
    }

    @Override
    public Optional<Category> findByName(String name) {
        var query = entityManager.createQuery("SELECT category FROM Category category " +
                "WHERE UPPER(TRIM(name))=UPPER(TRIM(:name))", Category.class);
        query.setParameter("name", name);
        return Optional.ofNullable(query.getSingleResult());
    }
}