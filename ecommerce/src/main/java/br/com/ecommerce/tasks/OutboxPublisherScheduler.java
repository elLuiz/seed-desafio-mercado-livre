package br.com.ecommerce.tasks;

import br.com.ecommerce.infrastructure.listener.EventRepository;
import br.com.ecommerce.infrastructure.publisher.RabbitMQPublisher;
import br.com.ecommerce.tasks.model.Event;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class OutboxPublisherScheduler {
    private static final Logger LOGGER = Logger.getLogger(OutboxPublisherScheduler.class.getSimpleName());
    private final EventRepository eventRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private final Environment environment;

    public OutboxPublisherScheduler(EventRepository eventRepository,
                                    RabbitMQPublisher rabbitMQPublisher,
                                    Environment environment) {
        this.eventRepository = eventRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
        this.environment = environment;
    }

    /**
     * Questions:
     * 1 - As soon as publish the event, should I mark as published in the database?
     * 2 - What if I manage to publish the event but fail to mark it as published in the database?
     * 3 - Are the consumers idempotent to handle duplicate messages (same eventId)?
     */
    @Scheduled(fixedRate = 2_000)
    @Transactional
    public void publishEvents() {
        List<Event> events = this.eventRepository.loadUnpublished();
        for (Event event : events) {
            LOGGER.log(Level.INFO, "Event: {0}", event);
            this.rabbitMQPublisher.publish(event.payload(), environment.getProperty(event.topic()), event.topic());
            this.eventRepository.published(event.id());
        }
    }
}