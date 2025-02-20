package br.com.ecommerce.service.common;

import java.time.OffsetDateTime;

public record StorageFile(String fileName, String url, OffsetDateTime uploadedAt) {
}