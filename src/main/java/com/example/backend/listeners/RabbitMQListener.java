package com.example.backend.listeners;

import com.example.backend.configurations.RabbitMQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitMQListener {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQListener.class);

    /**
     * Listen for messages on the default queue
     *
     * @param message The message received
     */
    @RabbitListener(queues = RabbitMQConfiguration.DEFAULT_QUEUE)
    public void receiveFromDefaultQueue(String message) {
        LOG.info("Received message from default queue: {}", message);
        // Process the message here
    }

    /**
     * Listen for messages on the direct queue
     *
     * @param message The message received
     */
    @RabbitListener(queues = RabbitMQConfiguration.DIRECT_QUEUE)
    public void receiveFromDirectQueue(String message) {
        LOG.info("Received message from direct queue: {}", message);
        // Process the message here
    }

    /**
     * Listen for messages on the fanout queue 1
     *
     * @param message The message received
     */
    @RabbitListener(queues = RabbitMQConfiguration.FANOUT_QUEUE_1)
    public void receiveFromFanoutQueue1(String message) {
        LOG.info("Received message from fanout queue 1: {}", message);
        // Process the message here
    }

    /**
     * Listen for messages on the fanout queue 2
     *
     * @param message The message received
     */
    @RabbitListener(queues = RabbitMQConfiguration.FANOUT_QUEUE_2)
    public void receiveFromFanoutQueue2(String message) {
        LOG.info("Received message from fanout queue 2: {}", message);
        // Process the message here
    }

    /**
     * Listen for messages on the topic queue 1
     *
     * @param message The message received
     */
    @RabbitListener(queues = RabbitMQConfiguration.TOPIC_QUEUE_1)
    public void receiveFromTopicQueue1(String message) {
        LOG.info("Received message from topic queue 1: {}", message);
        // Process the message here
    }

    /**
     * Listen for messages on the topic queue 2
     *
     * @param message The message received
     */
    @RabbitListener(queues = RabbitMQConfiguration.TOPIC_QUEUE_2)
    public void receiveFromTopicQueue2(String message) {
        LOG.info("Received message from topic queue 2: {}", message);
        // Process the message here
    }

    /**
     * Listen for messages on the headers queue
     *
     * @param message The message received
     * @param headers The headers from the message
     */
    @RabbitListener(queues = RabbitMQConfiguration.HEADERS_QUEUE)
    public void receiveFromHeadersQueue(@Payload String message, @Header Map<String, Object> headers) {
        LOG.info("Received message from headers queue: {} with headers: {}", message, headers);
        // Process the message here
    }

    /**
     * Listen for messages on the dead letter queue
     *
     * @param message The message received
     */
    @RabbitListener(queues = RabbitMQConfiguration.DEAD_LETTER_QUEUE)
    public void receiveFromDeadLetterQueue(Message message) {
        LOG.info("Received message from dead letter queue: {}", message);
        // Process the message here, possibly for error handling or retry logic
    }

    /**
     * Example of a listener with manual acknowledgment
     *
     * @param message The message received
     * @param channel The channel to acknowledge on
     * @param tag     The delivery tag
     */
    @RabbitListener(queues = RabbitMQConfiguration.DEFAULT_QUEUE, ackMode = "MANUAL")
    public void receiveWithManualAck(String message, com.rabbitmq.client.Channel channel, @Header(name = "amqp_deliveryTag") long tag) {
        try {
            LOG.info("Received message with manual ack: {}", message);
            // Process the message here

            // Acknowledge the message
            channel.basicAck(tag, false);
        } catch (Exception e) {
            try {
                // Reject the message and requeue it
                channel.basicNack(tag, false, true);
            } catch (Exception ex) {
                LOG.error("Error rejecting message: {}", ex.getMessage(), ex);
            }
        }
    }
}
