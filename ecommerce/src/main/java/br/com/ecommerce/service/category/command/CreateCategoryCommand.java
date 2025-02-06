package br.com.ecommerce.service.category.command;

import br.com.ecommerce.domain.common.unique.Unique;
import br.com.ecommerce.domain.model.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateCategoryCommand(@NotBlank(message = "category.must.not.be.empty")
                                    @Size(max = 255, message = "category.surpasses.limit")
                                    @Unique(ownerClass = Category.class, name = "name", message = "category.name.must.be.unique") String name,
                                    @Positive(message = "invalid.parent.id") Long parentId) {
    public Category toModel() {
        return new Category(name);
    }
}