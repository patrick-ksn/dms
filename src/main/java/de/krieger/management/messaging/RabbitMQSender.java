package de.krieger.management.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.delete.author.queue.name}")
    private String queueName;

    @Autowired
    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCommand(int authorId) {
        String message = Integer.toString(authorId);
        rabbitTemplate.convertAndSend(queueName, message);
        log.debug("Sent command: for author with ID: " + authorId + " to queue: " + queueName);
    }
}
