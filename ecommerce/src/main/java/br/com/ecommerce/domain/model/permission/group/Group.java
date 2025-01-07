package br.com.ecommerce.domain.model.permission.group;

import br.com.ecommerce.domain.model.GenericEntity;
import br.com.ecommerce.domain.model.permission.role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@Table(name = "tb_group")
public class Group extends GenericEntity {
    @Column(name = "group_name", nullable = false, length = 120)
    private String groupName;
    @Column(name = "group_acronym", nullable = false, length = 150)
    private String groupAcronym;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_group_role",
            joinColumns = @JoinColumn(name = "fk_group_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_role_id")
    )
    private Set<Role> roles;

    Group() {}

    public Group(String groupAcronym, String groupName) {
        this.groupAcronym = groupAcronym;
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(groupName, group.groupName) && Objects.equals(groupAcronym, group.groupAcronym);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, groupAcronym);
    }
}
