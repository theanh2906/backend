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
   * Listen for string messages on the backend topic
   *
   * @param message The message received
   * @param record  The consumer record containing metadata
   * @param ack     Manual acknowledgment
   */
  @KafkaListener(
    topics = KafkaConfiguration.BACKEND_TOPIC,
    groupId = "backend-group",
    containerFactory = "stringKafkaListenerContainerFactory"
  )
  public void receiveStringFromBackendTopic(@Payload String message,
                                            ConsumerRecord<String, String> record,
                                            Acknowledgment ack) {
    try {
      LOG.info("Received string message from backend topic: [{}] with key: [{}], partition: [{}], offset: [{}]",
        message, record.key(), record.partition(), record.offset());

      // Process the string message here
      processBackendStringMessage(message, record.key());

      // Manual acknowledgment
      ack.acknowledge();
      LOG.debug("String message acknowledged successfully");
    } catch (Exception e) {
      LOG.error("Error processing string message from backend topic: {}", e.getMessage(), e);
      // Don't acknowledge on error - message will be retried
    }
  }


  /**
     * Process messages from the default topic
     *
     * @param message The message content
     * @param key     The message key
     */
    private void processBackendMessage(String message, String key) {
        LOG.info("Processing default message: [{}] with key: [{}]", message, key);
        // Add your business logic here
        // For example: save to database, call other services, etc.
    }

  /**
   * Process string messages from the backend topic
   *
   * @param message The message content
   * @param key     The message key
   */
  private void processBackendStringMessage(String message, String key) {
    LOG.info("Processing string message: [{}] with key: [{}]", message, key);
    // Add your business logic here for string messages
  }

}
