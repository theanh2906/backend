package com.example.backend.rest;

import com.example.backend.configurations.RabbitMQConfiguration;
import com.example.backend.dtos.ResponseDto;
import com.example.backend.services.RabbitMQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rabbitmq")
public class RabbitMQController {

    public RabbitMQController(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    // Exchange and queue information
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getRabbitMQInfo() {
        Map<String, Object> info = new HashMap<>();

        // Add exchange information
        Map<String, String> exchanges = new HashMap<>();
        exchanges.put("direct", RabbitMQConfiguration.DIRECT_EXCHANGE);
        info.put("exchanges", exchanges);

        // Add queue information
        Map<String, String> queues = new HashMap<>();
        queues.put("default", RabbitMQConfiguration.DEFAULT_QUEUE);
        info.put("queues", queues);

        // Add routing key information
        Map<String, String> routingKeys = new HashMap<>();
        routingKeys.put("direct", RabbitMQConfiguration.DIRECT_ROUTING_KEY);
        info.put("routingKeys", routingKeys);

        return ResponseEntity.ok(info);
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
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQController.class);
    private final RabbitMQService rabbitMQService;
}