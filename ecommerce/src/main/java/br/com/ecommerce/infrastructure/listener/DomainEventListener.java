package br.com.ecommerce.infrastructure.listener;

import br.com.ecommerce.domain.model.common.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
class DomainEventListener {
    private static final Logger LOGGER = Logger.getLogger(DomainEventListener.class.getName());
    private final ObjectMapper objectMapper;

    public DomainEventListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onApplicationEvent(DomainEvent event) {
        try {
            LOGGER.log(Level.INFO, "Received event: {0}", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}