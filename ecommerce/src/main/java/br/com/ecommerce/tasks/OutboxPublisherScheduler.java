package br.com.ecommerce.tasks;

import br.com.ecommerce.tasks.service.PublisherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxPublisherScheduler {
    private final PublisherService publisherService;

    public OutboxPublisherScheduler(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * Questions:
     * 1 - As soon as publish the event, should I mark as published in the database?
     * 2 - What if I manage to publish the event but fail to mark it as published in the database?
     * 3 - Are the consumers idempotent to handle duplicate messages (same eventId)?
     */
    @Scheduled(fixedRate = 2_000)
    public void publishEvents() {
      this.publisherService.publishEvents();
    }
}