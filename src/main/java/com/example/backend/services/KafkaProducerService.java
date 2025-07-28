package com.example.backend.services;

import com.example.backend.configurations.KafkaConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Send a message to the default topic
     *
     * @param message The message to send
     */
    public void sendToDefaultTopic(String message) {
        sendMessage(KafkaConfiguration.DEFAULT_TOPIC, message);
    }

    /**
     * Send a message to the default topic with a specific key
     *
     * @param key     The message key
     * @param message The message to send
     */
    public void sendToDefaultTopic(String key, String message) {
        sendMessage(KafkaConfiguration.DEFAULT_TOPIC, key, message);
    }

    /**
     * Send a notification message
     *
     * @param message The notification message to send
     */
    public void sendNotification(String message) {
        sendMessage(KafkaConfiguration.NOTIFICATION_TOPIC, message);
    }

    /**
     * Send a notification message with a specific key
     *
     * @param key     The message key
     * @param message The notification message to send
     */
    public void sendNotification(String key, String message) {
        sendMessage(KafkaConfiguration.NOTIFICATION_TOPIC, key, message);
    }

    /**
     * Send a user event message
     *
     * @param userId The user ID as key
     * @param event  The event data
     */
    public void sendUserEvent(String userId, Object event) {
        sendMessage(KafkaConfiguration.USER_EVENTS_TOPIC, userId, event);
    }

    /**
     * Send a message to a specific topic
     *
     * @param topic   The topic to send to
     * @param message The message to send
     */
    public void sendMessage(String topic, Object message) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    LOG.info("Sent message=[{}] to topic=[{}] with offset=[{}]", 
                            message, topic, result.getRecordMetadata().offset());
                } else {
                    LOG.error("Unable to send message=[{}] to topic=[{}] due to: {}", 
                            message, topic, ex.getMessage());
                }
            });
        } catch (Exception e) {
            LOG.error("Error sending message to topic [{}]: {}", topic, e.getMessage(), e);
        }
    }

    /**
     * Send a message to a specific topic with a key
     *
     * @param topic   The topic to send to
     * @param key     The message key
     * @param message The message to send
     */
    public void sendMessage(String topic, String key, Object message) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    LOG.info("Sent message=[{}] with key=[{}] to topic=[{}] with offset=[{}]", 
                            message, key, topic, result.getRecordMetadata().offset());
                } else {
                    LOG.error("Unable to send message=[{}] with key=[{}] to topic=[{}] due to: {}", 
                            message, key, topic, ex.getMessage());
                }
            });
        } catch (Exception e) {
            LOG.error("Error sending message with key [{}] to topic [{}]: {}", key, topic, e.getMessage(), e);
        }
    }

    /**
     * Send a message synchronously to a specific topic
     *
     * @param topic   The topic to send to
     * @param message The message to send
     * @return SendResult containing metadata about the sent message
     * @throws Exception if sending fails
     */
    public SendResult<String, Object> sendMessageSync(String topic, Object message) throws Exception {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
            SendResult<String, Object> result = future.get();
            LOG.info("Sent message=[{}] to topic=[{}] synchronously with offset=[{}]", 
                    message, topic, result.getRecordMetadata().offset());
            return result;
        } catch (Exception e) {
            LOG.error("Error sending message synchronously to topic [{}]: {}", topic, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Send a message synchronously to a specific topic with a key
     *
     * @param topic   The topic to send to
     * @param key     The message key
     * @param message The message to send
     * @return SendResult containing metadata about the sent message
     * @throws Exception if sending fails
     */
    public SendResult<String, Object> sendMessageSync(String topic, String key, Object message) throws Exception {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
            SendResult<String, Object> result = future.get();
            LOG.info("Sent message=[{}] with key=[{}] to topic=[{}] synchronously with offset=[{}]", 
                    message, key, topic, result.getRecordMetadata().offset());
            return result;
        } catch (Exception e) {
            LOG.error("Error sending message synchronously with key [{}] to topic [{}]: {}", key, topic, e.getMessage(), e);
            throw e;
        }
    }
}