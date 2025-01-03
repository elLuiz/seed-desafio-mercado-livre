package br.com.ecommerce.application.common;

public record ErrorDescription(String fieldId, String code, String message) {
}