package br.com.ecommerce.application.login.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
