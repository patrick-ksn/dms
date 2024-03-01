package de.krieger.management.messaging;

import de.krieger.management.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQReceiver.class);
    private AuthorService authorService;

    public RabbitMQReceiver(AuthorService authorService)
    {
        this.authorService = authorService;
    }

    @Value("${rabbitmq.delete.author.queue.name}")
    private String queueName;

    @RabbitListener(queues = "${rabbitmq.delete.author.queue.name}")
    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000))
    public void receiveMessage(String message) {
        int authorId = Integer.parseInt(message);
        log.debug("Received authorID: " + authorId + " from queue: " + queueName);
        try {
            authorService.delete(authorId);
        } catch (Exception e) {
            log.debug("author not found. {}", authorId);
        }
    }



}
