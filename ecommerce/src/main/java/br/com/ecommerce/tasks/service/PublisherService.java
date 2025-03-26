package br.com.ecommerce.tasks.service;

import br.com.ecommerce.infrastructure.listener.EventRepository;
import br.com.ecommerce.infrastructure.publisher.RabbitMQPublisher;
import br.com.ecommerce.tasks.model.Event;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PublisherService {
    private static final Logger LOGGER = Logger.getLogger(PublisherService.class.getSimpleName());
    private final EventRepository eventRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private final Environment environment;

    public PublisherService(EventRepository eventRepository,
                                    RabbitMQPublisher rabbitMQPublisher,
                                    Environment environment) {
        this.eventRepository = eventRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
        this.environment = environment;
    }

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