package com.example.backend.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver {
    public void receiveMessage(String message) {
        System.err.println("Received <" + message + ">");
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

}
