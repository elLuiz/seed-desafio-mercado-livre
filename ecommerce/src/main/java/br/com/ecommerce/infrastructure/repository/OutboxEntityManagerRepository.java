package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.common.DomainEvent;
import br.com.ecommerce.infrastructure.listener.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
class OutboxEntityManagerRepository implements EventRepository {
    private static final Logger LOGGER = Logger.getLogger(OutboxEntityManagerRepository.class.getName());
    @PersistenceContext
    EntityManager entityManager;
    private final ObjectMapper objectMapper;
    private final Environment environment;

    public OutboxEntityManagerRepository(ObjectMapper objectMapper, Environment environment) {
        this.objectMapper = objectMapper;
        this.environment = environment;
    }

    @Override
    public <I> void addEvent(DomainEvent<I> domainEvent) throws Exception {
        if (domainEvent != null) {
            String payload = objectMapper.writeValueAsString(domainEvent);
            String query = """
                    INSERT INTO {h-schema}tb_outbox_table(topic, aggregate_id, aggregate_type, payload)
                    VALUES (:topic, :aggregateId, :aggregateType, CAST(:payload AS jsonb))
                    """;
            int insertedRows = entityManager.createNativeQuery(query)
                    .setParameter("topic", environment.getProperty(domainEvent.getDescription()))
                    .setParameter("aggregateId", domainEvent.getId())
                    .setParameter("aggregateType", domainEvent.getType())
                    .setParameter("payload", payload)
                    .executeUpdate();
            LOGGER.log(Level.INFO, "Inserted {0} event(s)", insertedRows);
        }
    }

    @Override
    public <I> Long countEventsByAggregateId(I id) {
        String query = """
                SELECT count(1) FROM {h-schema}tb_outbox_table 
                WHERE aggregate_id=:aggregateId
                """;
        Object result = entityManager.createNativeQuery(query)
                .setParameter("aggregateId", id.toString())
                .getSingleResult();
        return (Long) result;
    }
}