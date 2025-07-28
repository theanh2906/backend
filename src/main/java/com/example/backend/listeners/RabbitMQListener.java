package com.example.backend.listeners;

import com.example.backend.configurations.RabbitMQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Profile("prod")
@Component
public class RabbitMQListener {

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
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQListener.class);
}
