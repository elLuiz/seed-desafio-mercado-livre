package br.com.ecommerce.application.user.dto;

import br.com.ecommerce.domain.model.user.User;

import java.time.format.DateTimeFormatter;

public record UserCreatedResponse(Long id, String fullName, String createdAt) {
    public static UserCreatedResponse toResponse(User user) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");
        return new UserCreatedResponse(user.getId(), user.getFullName(), dateTimeFormatter.format(user.getCreatedAt()));
    }
}