package com.example.backend.listeners;

import com.example.backend.shared.Constant;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SQSMessageListener {
    @SqsListener(value = Constant.AWSConfig.AWS_SQS_QUEUE_NAME)
    public void handleMessage(@Payload String body) {
        System.err.println("Received message " + body);
    }
}
