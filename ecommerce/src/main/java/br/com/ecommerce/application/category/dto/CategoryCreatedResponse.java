package br.com.ecommerce.application.category.dto;

import br.com.ecommerce.domain.model.category.Category;

public record CategoryCreatedResponse(Long id, String name, String parentCategory) {
    public static CategoryCreatedResponse toResponse(Category category) {
        if (category.hasParent()) {
            return new CategoryCreatedResponse(category.getId(), category.getName(), category.getParent().getName());
        }
        return new CategoryCreatedResponse(category.getId(), category.getName(), null);
    }
}