package com.example.backend.services;

import com.example.backend.configurations.RabbitMQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Send a message to the default queue
     *
     * @param message The message to send
     */
    public void sendToDefaultQueue(Object message) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfiguration.DEFAULT_QUEUE, message);
            LOG.info("Message sent to default queue: {}", message);
        } catch (AmqpException e) {
            LOG.error("Error sending message to default queue: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Send a message to a direct exchange
     *
     * @param message The message to send
     */
    public void sendToDirectExchange(Object message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.DIRECT_EXCHANGE,
                    RabbitMQConfiguration.DIRECT_ROUTING_KEY,
                    message
            );
            LOG.info("Message sent to direct exchange: {}", message);
        } catch (AmqpException e) {
            LOG.error("Error sending message to direct exchange: {}", e.getMessage(), e);
            throw e;
        }
    }
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQService.class);
    private final RabbitTemplate rabbitTemplate;
}
