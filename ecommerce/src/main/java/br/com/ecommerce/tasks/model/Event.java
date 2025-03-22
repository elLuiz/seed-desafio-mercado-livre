package br.com.ecommerce.tasks.model;

public record Event(Long id, String topic, String aggregateId, String payload) {
}