package br.com.ecommerce.infrastructure.listener;

import br.com.ecommerce.domain.model.common.DomainEvent;
import br.com.ecommerce.tasks.model.Event;

import java.util.List;

public interface EventRepository {
    <I> void addEvent(DomainEvent<I> event) throws Exception;
    <I> Long countEventsByAggregateId(I id);
    List<Event> loadUnpublished();
    void published(Long id);
}