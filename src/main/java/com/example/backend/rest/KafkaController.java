package com.example.backend.rest;

import com.example.backend.configurations.KafkaConfiguration;
import com.example.backend.services.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    /**
     * Send a message to the default topic
     *
     * @param message The message to send
     * @return Response with status
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody String message) {
        try {
            kafkaProducerService.sendToDefaultTopic(message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent to default topic successfully");
            response.put("topic", KafkaConfiguration.DEFAULT_TOPIC);
            response.put("data", message);
            
            LOG.info("Message sent to default topic via REST API: {}", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error sending message to default topic: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send message: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Send a message with a key to the default topic
     *
     * @param request The request containing key and message
     * @return Response with status
     */
    @PostMapping("/send-with-key")
    public ResponseEntity<Map<String, Object>> sendMessageWithKey(@RequestBody Map<String, String> request) {
        try {
            String key = request.get("key");
            String message = request.get("message");
            
            if (key == null || message == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Both 'key' and 'message' fields are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            kafkaProducerService.sendToDefaultTopic(key, message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent to default topic with key successfully");
            response.put("topic", KafkaConfiguration.DEFAULT_TOPIC);
            response.put("key", key);
            response.put("data", message);
            
            LOG.info("Message sent to default topic with key [{}] via REST API: {}", key, message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error sending message with key to default topic: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send message: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Send a notification message
     *
     * @param message The notification message to send
     * @return Response with status
     */
    @PostMapping("/notification")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody String message) {
        try {
            kafkaProducerService.sendNotification(message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Notification sent successfully");
            response.put("topic", KafkaConfiguration.NOTIFICATION_TOPIC);
            response.put("data", message);
            
            LOG.info("Notification sent via REST API: {}", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error sending notification: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send notification: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Send a user event
     *
     * @param request The request containing userId and event data
     * @return Response with status
     */
    @PostMapping("/user-event")
    public ResponseEntity<Map<String, Object>> sendUserEvent(@RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("userId");
            Object eventData = request.get("eventData");
            
            if (userId == null || eventData == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Both 'userId' and 'eventData' fields are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            kafkaProducerService.sendUserEvent(userId, eventData);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "User event sent successfully");
            response.put("topic", KafkaConfiguration.USER_EVENTS_TOPIC);
            response.put("userId", userId);
            response.put("eventData", eventData);
            
            LOG.info("User event sent for user [{}] via REST API: {}", userId, eventData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error sending user event: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send user event: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Send a message to a custom topic
     *
     * @param request The request containing topic, key (optional), and message
     * @return Response with status
     */
    @PostMapping("/send-to-topic")
    public ResponseEntity<Map<String, Object>> sendToTopic(@RequestBody Map<String, Object> request) {
        try {
            String topic = (String) request.get("topic");
            String key = (String) request.get("key");
            Object message = request.get("message");
            
            if (topic == null || message == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Both 'topic' and 'message' fields are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (key != null) {
                kafkaProducerService.sendMessage(topic, key, message);
            } else {
                kafkaProducerService.sendMessage(topic, message);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent to custom topic successfully");
            response.put("topic", topic);
            if (key != null) {
                response.put("key", key);
            }
            response.put("data", message);
            
            LOG.info("Message sent to custom topic [{}] via REST API: {}", topic, message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error sending message to custom topic: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send message: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Send a message synchronously to a topic
     *
     * @param request The request containing topic, key (optional), and message
     * @return Response with status and metadata
     */
    @PostMapping("/send-sync")
    public ResponseEntity<Map<String, Object>> sendMessageSync(@RequestBody Map<String, Object> request) {
        try {
            String topic = (String) request.get("topic");
            String key = (String) request.get("key");
            Object message = request.get("message");
            
            if (topic == null || message == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Both 'topic' and 'message' fields are required");
                return ResponseEntity.badRequest().body(response);
            }
            
            SendResult<String, Object> result;
            if (key != null) {
                result = kafkaProducerService.sendMessageSync(topic, key, message);
            } else {
                result = kafkaProducerService.sendMessageSync(topic, message);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent synchronously");
            response.put("topic", topic);
            if (key != null) {
                response.put("key", key);
            }
            response.put("data", message);
            response.put("partition", result.getRecordMetadata().partition());
            response.put("offset", result.getRecordMetadata().offset());
            response.put("timestamp", result.getRecordMetadata().timestamp());
            
            LOG.info("Message sent synchronously to topic [{}] via REST API: {}", topic, message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error sending message synchronously: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send message synchronously: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get Kafka configuration information
     *
     * @return Kafka configuration details
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getKafkaInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Kafka configuration information");
        
        Map<String, String> topics = new HashMap<>();
        topics.put("default", KafkaConfiguration.DEFAULT_TOPIC);
        topics.put("notification", KafkaConfiguration.NOTIFICATION_TOPIC);
        topics.put("userEvents", KafkaConfiguration.USER_EVENTS_TOPIC);
        
        response.put("availableTopics", topics);
        response.put("endpoints", Map.of(
            "sendMessage", "/api/kafka/send",
            "sendWithKey", "/api/kafka/send-with-key",
            "sendNotification", "/api/kafka/notification",
            "sendUserEvent", "/api/kafka/user-event",
            "sendToTopic", "/api/kafka/send-to-topic",
            "sendSync", "/api/kafka/send-sync",
            "info", "/api/kafka/info"
        ));
        
        return ResponseEntity.ok(response);
    }
}