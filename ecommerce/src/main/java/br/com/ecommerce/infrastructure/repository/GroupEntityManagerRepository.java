package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.permission.group.Group;
import br.com.ecommerce.service.group.GroupRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class GroupEntityManagerRepository extends GenericRepository<Group> implements GroupRepository {
    public GroupEntityManagerRepository() {
        super(Group.class);
    }

    public Optional<Group> findByAcronym(String acronym) {
        Group group = entityManager.createQuery("""
                SELECT DISTINCT group FROM Group group
                JOIN FETCH group.roles
                WHERE group.groupAcronym = UPPER(:acronym)""", Group.class)
                .setParameter("acronym", acronym)
                .getSingleResult();
        return Optional.ofNullable(group);
    }
}