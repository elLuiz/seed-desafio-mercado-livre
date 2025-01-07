package br.com.ecommerce.service.group;

import br.com.ecommerce.domain.model.permission.group.Group;

import java.util.Optional;

public interface GroupRepository {
    Optional<Group> findByAcronym(String acronym);
}
