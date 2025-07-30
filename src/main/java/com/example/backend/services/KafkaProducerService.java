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
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Send a message to the default topic
     *
     * @param message The message to send
     */
    public void sendToBackendTopic(String message) {
        sendMessage(KafkaConfiguration.BACKEND_TOPIC, message);
    }

    /**
     * Send a message to the default topic with a specific key
     *
     * @param key     The message key
     * @param message The message to send
     */
    public void sendToBackendTopic(String key, String message) {
        sendMessage(KafkaConfiguration.BACKEND_TOPIC, key, message);
    }

    /**
     * Send a message to a specific topic
     *
     * @param topic   The topic to send to
     * @param message The message to send
     */
    public void sendMessage(String topic, String message) {
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
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
    public void sendMessage(String topic, String key, String message) {
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, message);
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
    public SendResult<String, String> sendMessageSync(String topic, String message) throws Exception {
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
            SendResult<String, String> result = future.get();
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
    public SendResult<String, String> sendMessageSync(String topic, String key, String message) throws Exception {
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, message);
            SendResult<String, String> result = future.get();
            LOG.info("Sent message=[{}] with key=[{}] to topic=[{}] synchronously with offset=[{}]",
                    message, key, topic, result.getRecordMetadata().offset());
            return result;
        } catch (Exception e) {
            LOG.error("Error sending message synchronously with key [{}] to topic [{}]: {}", key, topic, e.getMessage(), e);
            throw e;
        }
    }
}
