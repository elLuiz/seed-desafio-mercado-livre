package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.common.unique.Unique;
import br.com.ecommerce.domain.model.GenericEntity;
import br.com.ecommerce.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenericUniqueConstraintValidator implements ConstraintValidator<Unique, String> {
    private Class<? extends GenericEntity> entityClass;
    private String fieldName;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.entityClass = constraintAnnotation.ownerClass();
        this.fieldName = constraintAnnotation.name();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isNullOrEmpty(value)) {
            return true;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<? extends GenericEntity> root = query.from(entityClass);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(root.get(fieldName)), value.trim().toUpperCase());
        query.select(criteriaBuilder.count(root.get("id"))).where(predicate);
        Long rows = entityManager.createQuery(query).getSingleResult();
        return rows == null || rows == 0L;
    }
}