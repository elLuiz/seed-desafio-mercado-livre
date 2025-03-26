package br.com.ecommerce.infrastructure.repository;

import br.com.ecommerce.domain.model.common.DomainEvent;
import br.com.ecommerce.infrastructure.listener.EventRepository;
import br.com.ecommerce.tasks.converter.EventTupleConverter;
import br.com.ecommerce.tasks.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
class OutboxEntityManagerRepository implements EventRepository {
    private static final Logger LOGGER = Logger.getLogger(OutboxEntityManagerRepository.class.getName());
    @PersistenceContext
    EntityManager entityManager;
    private final ObjectMapper objectMapper;

    public OutboxEntityManagerRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
                    .setParameter("topic", domainEvent.getDescription())
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

    @Override
    public List<Event> loadUnpublished() {
        String query = """
                SELECT id, topic, aggregate_id, payload, published
                FROM {h-schema}tb_outbox_table
                WHERE published=false
                ORDER BY id DESC
                """;
        List<Tuple> tuples = entityManager.createNativeQuery(query, Tuple.class).getResultList();
        return EventTupleConverter.convert(tuples);
    }

    @Override
    public void published(Long id) {
        String query = """
                UPDATE {h-schema}tb_outbox_table SET published=true
                WHERE id=:id
                """;
        int rows = entityManager.createNativeQuery(query)
                .setParameter("id", id)
                .executeUpdate();
        if (rows > 0) {
            LOGGER.log(Level.INFO, "Successfully updated event {0} to published", id);
        } else {
            LOGGER.log(Level.WARNING, "Could not update event {0}", id);
        }
    }
}