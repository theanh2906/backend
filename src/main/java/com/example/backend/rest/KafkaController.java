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
            kafkaProducerService.sendToBackendTopic(message);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent to default topic successfully");
            response.put("topic", KafkaConfiguration.BACKEND_TOPIC);
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

            kafkaProducerService.sendToBackendTopic(key, message);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent to default topic with key successfully");
            response.put("topic", KafkaConfiguration.BACKEND_TOPIC);
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

            String messageStr = message.toString();
            if (key != null) {
                kafkaProducerService.sendMessage(topic, key, messageStr);
            } else {
                kafkaProducerService.sendMessage(topic, messageStr);
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

            String messageStr = message.toString();
            SendResult<String, String> result;
            if (key != null) {
                result = kafkaProducerService.sendMessageSync(topic, key, messageStr);
            } else {
                result = kafkaProducerService.sendMessageSync(topic, messageStr);
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
}
