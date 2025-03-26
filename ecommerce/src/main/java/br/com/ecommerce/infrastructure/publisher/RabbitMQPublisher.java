package br.com.ecommerce.infrastructure.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPublisher {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String payload, String exchange, String routingKey) {
        this.rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }
}