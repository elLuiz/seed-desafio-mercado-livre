package br.com.ecommerce.infrastructure.listener;

import br.com.ecommerce.domain.model.common.DomainEvent;

public interface EventRepository {
    <I> void addEvent(DomainEvent<I> event) throws Exception;
    <I> Long countEventsByAggregateId(I id);
}