package com.example.backend.services;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Service
public class RabbitMQStreamService {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQStreamService.class);
    private final ConnectionFactory connectionFactory;
    private final Map<String, Channel> streamChannels = new ConcurrentHashMap<>();

    public RabbitMQStreamService(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Create a stream (queue with stream-like properties)
     *
     * @param streamName The name of the stream
     * @throws IOException      If an I/O error occurs
     * @throws TimeoutException If a timeout occurs
     */
    public void createStream(String streamName) throws IOException, TimeoutException {
        try (Connection connection = connectionFactory.createConnection().getDelegate();
             Channel channel = connection.createChannel()) {

            // Set queue arguments for stream-like behavior
            Map<String, Object> args = new HashMap<>();
            args.put("x-queue-type", "stream"); // Use stream queue type if supported by RabbitMQ server
            args.put("x-max-length-bytes", 2_000_000_000); // 2GB
            args.put("x-stream-max-segment-size-bytes", 100_000_000); // 100MB segments

            // Declare the queue with stream properties
            channel.queueDeclare(streamName, true, false, false, args);
            LOG.info("Stream created: {}", streamName);
        }
    }

    /**
     * Publish a message to a stream
     *
     * @param streamName The name of the stream
     * @param message    The message to publish
     * @throws IOException      If an I/O error occurs
     * @throws TimeoutException If a timeout occurs
     */
    public void publishToStream(String streamName, String message) throws IOException, TimeoutException {
        try (Connection connection = connectionFactory.createConnection().getDelegate();
             Channel channel = connection.createChannel()) {

            // Publish the message to the stream
            channel.basicPublish("", streamName, null, message.getBytes(StandardCharsets.UTF_8));
            LOG.info("Message published to stream {}: {}", streamName, message);
        }
    }

    /**
     * Publish multiple messages to a stream
     *
     * @param streamName The name of the stream
     * @param messages   The messages to publish
     * @throws IOException      If an I/O error occurs
     * @throws TimeoutException If a timeout occurs
     * @throws InterruptedException If the thread is interrupted while waiting for confirms
     */
    public void publishBatchToStream(String streamName, Iterable<String> messages) throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = connectionFactory.createConnection().getDelegate();
             Channel channel = connection.createChannel()) {

            // Enable publisher confirms
            channel.confirmSelect();

            // Publish all messages
            for (String message : messages) {
                channel.basicPublish("", streamName, null, message.getBytes(StandardCharsets.UTF_8));
            }

            // Wait for confirms
            if (channel.waitForConfirms()) {
                LOG.info("Batch published to stream {}", streamName);
            } else {
                LOG.error("Failed to publish batch to stream {}", streamName);
                throw new IOException("Failed to publish batch to stream");
            }
        }
    }

    /**
     * Consume messages from a stream
     *
     * @param streamName The name of the stream
     * @param consumer   The consumer to process messages
     * @return The consumer tag
     * @throws IOException      If an I/O error occurs
     * @throws TimeoutException If a timeout occurs
     */
    public String consumeFromStream(String streamName, Consumer<String> consumer) throws IOException, TimeoutException {
        Connection connection = connectionFactory.createConnection().getDelegate();
        Channel channel = connection.createChannel();

        // Store the channel for later use
        streamChannels.put(streamName, channel);

        // Set consumer arguments for stream-like behavior
        Map<String, Object> args = new HashMap<>();
        args.put("x-stream-offset", "first"); // Start from the beginning of the stream

        // Create a consumer
        String consumerTag = channel.basicConsume(streamName, false, args, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                try {
                    consumer.accept(message);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e) {
                    LOG.error("Error processing message from stream {}: {}", streamName, e.getMessage(), e);
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        });

        LOG.info("Started consuming from stream {} with consumer tag {}", streamName, consumerTag);
        return consumerTag;
    }

    /**
     * Stop consuming from a stream
     *
     * @param streamName  The name of the stream
     * @param consumerTag The consumer tag
     * @throws IOException If an I/O error occurs
     */
    public void stopConsuming(String streamName, String consumerTag) throws IOException {
        Channel channel = streamChannels.get(streamName);
        if (channel != null) {
            channel.basicCancel(consumerTag);
            LOG.info("Stopped consuming from stream {} with consumer tag {}", streamName, consumerTag);
        }
    }

    /**
     * Close all stream channels
     */
    public void closeAllStreamChannels() {
        for (Map.Entry<String, Channel> entry : streamChannels.entrySet()) {
            try {
                entry.getValue().close();
                LOG.info("Closed channel for stream {}", entry.getKey());
            } catch (IOException | TimeoutException e) {
                LOG.error("Error closing channel for stream {}: {}", entry.getKey(), e.getMessage(), e);
            }
        }
        streamChannels.clear();
    }

    /**
     * Asynchronously publish a message to a stream
     *
     * @param streamName The name of the stream
     * @param message    The message to publish
     * @return A CompletableFuture that completes when the message is published
     */
    public CompletableFuture<Void> publishToStreamAsync(String streamName, String message) {
        return CompletableFuture.runAsync(() -> {
            try {
                publishToStream(streamName, message);
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException("Error publishing to stream: " + e.getMessage(), e);
            }
        });
    }
}
