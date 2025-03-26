package br.com.ecommerce.infrastructure.listener;

import br.com.ecommerce.domain.model.common.DomainEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
class DomainEventListener {
    private static final Logger LOGGER = Logger.getLogger(DomainEventListener.class.getName());
    private final EventRepository eventRepository;

    public DomainEventListener(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public <I> void onApplicationEvent(DomainEvent<I> event) throws Exception {
        LOGGER.log(Level.INFO, "Received event: {0}", event.getEventId());
        this.eventRepository.addEvent(event);
    }
}