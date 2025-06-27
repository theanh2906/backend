package com.example.backend.services;

import com.example.backend.configurations.RabbitMQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitMQService {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQService.class);
    private final RabbitTemplate rabbitTemplate;

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

    /**
     * Send a message to a fanout exchange
     *
     * @param message The message to send
     */
    public void sendToFanoutExchange(Object message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.FANOUT_EXCHANGE,
                    "", // Routing key is ignored for fanout exchanges
                    message
            );
            LOG.info("Message sent to fanout exchange: {}", message);
        } catch (AmqpException e) {
            LOG.error("Error sending message to fanout exchange: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Send a message to a topic exchange
     *
     * @param message    The message to send
     * @param routingKey The routing key to use
     */
    public void sendToTopicExchange(Object message, String routingKey) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.TOPIC_EXCHANGE,
                    routingKey,
                    message
            );
            LOG.info("Message sent to topic exchange with routing key {}: {}", routingKey, message);
        } catch (AmqpException e) {
            LOG.error("Error sending message to topic exchange: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Send a message to a headers exchange
     *
     * @param message The message to send
     * @param headers The headers to include
     */
    public void sendToHeadersExchange(Object message, Map<String, Object> headers) {
        try {
            MessageProperties messageProperties = MessagePropertiesBuilder.newInstance()
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .copyHeaders(headers)
                    .build();

            Message amqpMessage = MessageBuilder
                    .withBody(rabbitTemplate.getMessageConverter().toMessage(message, messageProperties).getBody())
                    .andProperties(messageProperties)
                    .build();

            rabbitTemplate.send(RabbitMQConfiguration.HEADERS_EXCHANGE, "", amqpMessage);
            LOG.info("Message sent to headers exchange with headers {}: {}", headers, message);
        } catch (AmqpException e) {
            LOG.error("Error sending message to headers exchange: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Send a message with a delay
     *
     * @param message    The message to send
     * @param exchange   The exchange to send to
     * @param routingKey The routing key to use
     * @param delayMs    The delay in milliseconds
     */
    public void sendWithDelay(Object message, String exchange, String routingKey, long delayMs) {
        try {
            MessageProperties messageProperties = MessagePropertiesBuilder.newInstance()
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();

            // Set the x-delay header for delayed message exchange plugin
            messageProperties.setHeader("x-delay", delayMs);

            Message amqpMessage = MessageBuilder
                    .withBody(rabbitTemplate.getMessageConverter().toMessage(message, messageProperties).getBody())
                    .andProperties(messageProperties)
                    .build();

            rabbitTemplate.send(exchange, routingKey, amqpMessage);
            LOG.info("Message sent to exchange {} with routing key {} and delay {}: {}", 
                    exchange, routingKey, delayMs, message);
        } catch (AmqpException e) {
            LOG.error("Error sending delayed message: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Send a message with priority
     *
     * @param message    The message to send
     * @param exchange   The exchange to send to
     * @param routingKey The routing key to use
     * @param priority   The priority (0-9)
     */
    public void sendWithPriority(Object message, String exchange, String routingKey, int priority) {
        try {
            MessageProperties messageProperties = MessagePropertiesBuilder.newInstance()
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setPriority(priority)
                    .build();

            Message amqpMessage = MessageBuilder
                    .withBody(rabbitTemplate.getMessageConverter().toMessage(message, messageProperties).getBody())
                    .andProperties(messageProperties)
                    .build();

            rabbitTemplate.send(exchange, routingKey, amqpMessage);
            LOG.info("Message sent to exchange {} with routing key {} and priority {}: {}", 
                    exchange, routingKey, priority, message);
        } catch (AmqpException e) {
            LOG.error("Error sending prioritized message: {}", e.getMessage(), e);
            throw e;
        }
    }
}
