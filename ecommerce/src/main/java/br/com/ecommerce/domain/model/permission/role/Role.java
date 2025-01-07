package br.com.ecommerce.domain.model.permission.role;

import br.com.ecommerce.domain.model.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "tb_role")
public class Role extends GenericEntity {
    @Column(name = "role_description", nullable = false, length = 500)
    private String roleDescription;
    @Column(name = "role_acronym", nullable = false, length = 100)
    private String roleAcronym;

    Role() {}

    public Role(String roleAcronym, String roleDescription) {
        this.roleAcronym = roleAcronym;
        this.roleDescription = roleDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(getRoleAcronym(), role.getRoleAcronym());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getRoleAcronym());
    }
}