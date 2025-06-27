package com.example.backend.rest;

import com.example.backend.configurations.RabbitMQConfiguration;
import com.example.backend.dtos.ResponseDto;
import com.example.backend.services.RabbitMQService;
import com.example.backend.services.RabbitMQStreamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/rabbitmq")
public class RabbitMQController {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQController.class);
    private final RabbitMQService rabbitMQService;
    private final RabbitMQStreamService rabbitMQStreamService;

    public RabbitMQController(RabbitMQService rabbitMQService, RabbitMQStreamService rabbitMQStreamService) {
        this.rabbitMQService = rabbitMQService;
        this.rabbitMQStreamService = rabbitMQStreamService;
    }

    @PostMapping("/default-queue")
    public ResponseEntity<ResponseDto> sendToDefaultQueue(@RequestBody String message) {
        try {
            rabbitMQService.sendToDefaultQueue(message);
            return ResponseEntity.ok(new ResponseDto("Message sent to default queue"));
        } catch (Exception e) {
            LOG.error("Error sending message to default queue: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error sending message: " + e.getMessage()));
        }
    }

    @PostMapping("/direct-exchange")
    public ResponseEntity<ResponseDto> sendToDirectExchange(@RequestBody String message) {
        try {
            rabbitMQService.sendToDirectExchange(message);
            return ResponseEntity.ok(new ResponseDto("Message sent to direct exchange"));
        } catch (Exception e) {
            LOG.error("Error sending message to direct exchange: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error sending message: " + e.getMessage()));
        }
    }

    @PostMapping("/fanout-exchange")
    public ResponseEntity<ResponseDto> sendToFanoutExchange(@RequestBody String message) {
        try {
            rabbitMQService.sendToFanoutExchange(message);
            return ResponseEntity.ok(new ResponseDto("Message sent to fanout exchange"));
        } catch (Exception e) {
            LOG.error("Error sending message to fanout exchange: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error sending message: " + e.getMessage()));
        }
    }

    @PostMapping("/topic-exchange")
    public ResponseEntity<ResponseDto> sendToTopicExchange(
            @RequestBody String message,
            @RequestParam String routingKey) {
        try {
            rabbitMQService.sendToTopicExchange(message, routingKey);
            return ResponseEntity.ok(new ResponseDto("Message sent to topic exchange with routing key: " + routingKey));
        } catch (Exception e) {
            LOG.error("Error sending message to topic exchange: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error sending message: " + e.getMessage()));
        }
    }

    @PostMapping("/headers-exchange")
    public ResponseEntity<ResponseDto> sendToHeadersExchange(
            @RequestBody String message,
            @RequestParam Map<String, String> headers) {
        try {
            Map<String, Object> headerMap = new HashMap<>(headers);
            rabbitMQService.sendToHeadersExchange(message, headerMap);
            return ResponseEntity.ok(new ResponseDto("Message sent to headers exchange with headers: " + headers));
        } catch (Exception e) {
            LOG.error("Error sending message to headers exchange: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error sending message: " + e.getMessage()));
        }
    }

    @PostMapping("/delayed-message")
    public ResponseEntity<ResponseDto> sendDelayedMessage(
            @RequestBody String message,
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestParam long delayMs) {
        try {
            rabbitMQService.sendWithDelay(message, exchange, routingKey, delayMs);
            return ResponseEntity.ok(new ResponseDto("Message sent with delay: " + delayMs + "ms"));
        } catch (Exception e) {
            LOG.error("Error sending delayed message: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error sending message: " + e.getMessage()));
        }
    }

    @PostMapping("/priority-message")
    public ResponseEntity<ResponseDto> sendPriorityMessage(
            @RequestBody String message,
            @RequestParam String exchange,
            @RequestParam String routingKey,
            @RequestParam int priority) {
        try {
            rabbitMQService.sendWithPriority(message, exchange, routingKey, priority);
            return ResponseEntity.ok(new ResponseDto("Message sent with priority: " + priority));
        } catch (Exception e) {
            LOG.error("Error sending priority message: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error sending message: " + e.getMessage()));
        }
    }

    // Stream API endpoints
    @PostMapping("/streams/{streamName}")
    public ResponseEntity<ResponseDto> createStream(@PathVariable String streamName) {
        try {
            rabbitMQStreamService.createStream(streamName);
            return ResponseEntity.ok(new ResponseDto("Stream created: " + streamName));
        } catch (IOException | TimeoutException e) {
            LOG.error("Error creating stream: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error creating stream: " + e.getMessage()));
        }
    }

    @PostMapping("/streams/{streamName}/publish")
    public ResponseEntity<ResponseDto> publishToStream(
            @PathVariable String streamName,
            @RequestBody String message) {
        try {
            rabbitMQStreamService.publishToStream(streamName, message);
            return ResponseEntity.ok(new ResponseDto("Message published to stream: " + streamName));
        } catch (IOException | TimeoutException e) {
            LOG.error("Error publishing to stream: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error publishing to stream: " + e.getMessage()));
        }
    }

    @PostMapping("/streams/{streamName}/publish-batch")
    public ResponseEntity<ResponseDto> publishBatchToStream(
            @PathVariable String streamName,
            @RequestBody List<String> messages) {
        try {
            rabbitMQStreamService.publishBatchToStream(streamName, messages);
            return ResponseEntity.ok(new ResponseDto("Batch published to stream: " + streamName));
        } catch (IOException | TimeoutException | InterruptedException e) {
            LOG.error("Error publishing batch to stream: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseDto("Error publishing batch to stream: " + e.getMessage()));
        }
    }

    // Exchange and queue information
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getRabbitMQInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // Add exchange information
        Map<String, String> exchanges = new HashMap<>();
        exchanges.put("direct", RabbitMQConfiguration.DIRECT_EXCHANGE);
        exchanges.put("fanout", RabbitMQConfiguration.FANOUT_EXCHANGE);
        exchanges.put("topic", RabbitMQConfiguration.TOPIC_EXCHANGE);
        exchanges.put("headers", RabbitMQConfiguration.HEADERS_EXCHANGE);
        exchanges.put("deadLetter", RabbitMQConfiguration.DEAD_LETTER_EXCHANGE);
        info.put("exchanges", exchanges);
        
        // Add queue information
        Map<String, String> queues = new HashMap<>();
        queues.put("default", RabbitMQConfiguration.DEFAULT_QUEUE);
        queues.put("direct", RabbitMQConfiguration.DIRECT_QUEUE);
        queues.put("fanout1", RabbitMQConfiguration.FANOUT_QUEUE_1);
        queues.put("fanout2", RabbitMQConfiguration.FANOUT_QUEUE_2);
        queues.put("topic1", RabbitMQConfiguration.TOPIC_QUEUE_1);
        queues.put("topic2", RabbitMQConfiguration.TOPIC_QUEUE_2);
        queues.put("headers", RabbitMQConfiguration.HEADERS_QUEUE);
        queues.put("deadLetter", RabbitMQConfiguration.DEAD_LETTER_QUEUE);
        info.put("queues", queues);
        
        // Add routing key information
        Map<String, String> routingKeys = new HashMap<>();
        routingKeys.put("direct", RabbitMQConfiguration.DIRECT_ROUTING_KEY);
        routingKeys.put("topic1", RabbitMQConfiguration.TOPIC_ROUTING_KEY_1);
        routingKeys.put("topic2", RabbitMQConfiguration.TOPIC_ROUTING_KEY_2);
        info.put("routingKeys", routingKeys);
        
        return ResponseEntity.ok(info);
    }
}