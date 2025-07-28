package com.example.backend.listeners;

import com.example.backend.configurations.KafkaConfiguration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageListener.class);

    /**
     * Listen for messages on the default topic
     *
     * @param message The message received
     * @param record  The consumer record containing metadata
     * @param ack     Manual acknowledgment
     */
    @KafkaListener(topics = KafkaConfiguration.DEFAULT_TOPIC, groupId = "backend-group")
    public void receiveFromDefaultTopic(@Payload String message, 
                                       ConsumerRecord<String, String> record,
                                       Acknowledgment ack) {
        try {
            LOG.info("Received message from default topic: [{}] with key: [{}], partition: [{}], offset: [{}]", 
                    message, record.key(), record.partition(), record.offset());
            
            // Process the message here
            processDefaultMessage(message, record.key());
            
            // Manual acknowledgment
            ack.acknowledge();
            LOG.debug("Message acknowledged successfully");
        } catch (Exception e) {
            LOG.error("Error processing message from default topic: {}", e.getMessage(), e);
            // Don't acknowledge on error - message will be retried
        }
    }

    /**
     * Listen for messages on the notification topic
     *
     * @param message The message received
     * @param record  The consumer record containing metadata
     * @param ack     Manual acknowledgment
     */
    @KafkaListener(topics = KafkaConfiguration.NOTIFICATION_TOPIC, groupId = "backend-group")
    public void receiveNotification(@Payload String message,
                                   ConsumerRecord<String, String> record,
                                   Acknowledgment ack) {
        try {
            LOG.info("Received notification: [{}] with key: [{}] from topic: [{}], partition: [{}], offset: [{}]", 
                    message, record.key(), record.topic(), record.partition(), record.offset());
            
            // Process the notification
            processNotification(message, record.key());
            
            // Manual acknowledgment
            ack.acknowledge();
            LOG.debug("Notification acknowledged successfully");
        } catch (Exception e) {
            LOG.error("Error processing notification: {}", e.getMessage(), e);
            // Don't acknowledge on error - message will be retried
        }
    }

    /**
     * Listen for messages on the user events topic
     *
     * @param event  The event data received
     * @param record The consumer record containing metadata
     * @param ack    Manual acknowledgment
     */
    @KafkaListener(topics = KafkaConfiguration.USER_EVENTS_TOPIC, 
                   groupId = "backend-group",
                   containerFactory = "kafkaListenerContainerFactory")
    public void receiveUserEvent(@Payload Object event, 
                                ConsumerRecord<String, Object> record,
                                Acknowledgment ack) {
        try {
            LOG.info("Received user event: [{}] for user: [{}], partition: [{}], offset: [{}]", 
                    event, record.key(), record.partition(), record.offset());
            
            // Process the user event
            processUserEvent(record.key(), event);
            
            // Manual acknowledgment
            ack.acknowledge();
            LOG.debug("User event acknowledged successfully");
        } catch (Exception e) {
            LOG.error("Error processing user event: {}", e.getMessage(), e);
            // Don't acknowledge on error - message will be retried
        }
    }

    /**
     * Generic listener for any topic with string messages
     *
     * @param message The message received
     * @param record  The consumer record containing metadata
     * @param ack     Manual acknowledgment
     */
    @KafkaListener(topicPattern = ".*-topic", 
                   groupId = "backend-generic-group",
                   containerFactory = "stringKafkaListenerContainerFactory")
    public void receiveGenericMessage(@Payload String message,
                                     ConsumerRecord<String, String> record,
                                     Acknowledgment ack) {
        try {
            LOG.info("Received generic message: [{}] from topic: [{}] with key: [{}]", 
                    message, record.topic(), record.key());
            
            // Process based on topic
            processGenericMessage(record.topic(), record.key(), message);
            
            // Manual acknowledgment
            ack.acknowledge();
            LOG.debug("Generic message acknowledged successfully");
        } catch (Exception e) {
            LOG.error("Error processing generic message from topic [{}]: {}", record.topic(), e.getMessage(), e);
            // Don't acknowledge on error - message will be retried
        }
    }

    /**
     * Process messages from the default topic
     *
     * @param message The message content
     * @param key     The message key
     */
    private void processDefaultMessage(String message, String key) {
        LOG.info("Processing default message: [{}] with key: [{}]", message, key);
        // Add your business logic here
        // For example: save to database, call other services, etc.
    }

    /**
     * Process notification messages
     *
     * @param message The notification content
     * @param key     The message key
     */
    private void processNotification(String message, String key) {
        LOG.info("Processing notification: [{}] with key: [{}]", message, key);
        // Add your notification processing logic here
        // For example: send email, push notification, etc.
    }

    /**
     * Process user event messages
     *
     * @param userId The user ID
     * @param event  The event data
     */
    private void processUserEvent(String userId, Object event) {
        LOG.info("Processing user event for user: [{}], event: [{}]", userId, event);
        // Add your user event processing logic here
        // For example: update user statistics, trigger workflows, etc.
    }

    /**
     * Process generic messages based on topic
     *
     * @param topic   The topic name
     * @param key     The message key
     * @param message The message content
     */
    private void processGenericMessage(String topic, String key, String message) {
        LOG.info("Processing generic message from topic: [{}], key: [{}], message: [{}]", topic, key, message);
        
        // Route processing based on topic
        switch (topic) {
            case "audit-topic":
                processAuditMessage(key, message);
                break;
            case "error-topic":
                processErrorMessage(key, message);
                break;
            default:
                LOG.warn("Unknown topic: [{}], message: [{}]", topic, message);
        }
    }

    /**
     * Process audit messages
     *
     * @param key     The message key
     * @param message The audit message
     */
    private void processAuditMessage(String key, String message) {
        LOG.info("Processing audit message with key: [{}], message: [{}]", key, message);
        // Add audit processing logic here
    }

    /**
     * Process error messages
     *
     * @param key     The message key
     * @param message The error message
     */
    private void processErrorMessage(String key, String message) {
        LOG.error("Processing error message with key: [{}], message: [{}]", key, message);
        // Add error handling logic here
    }
}