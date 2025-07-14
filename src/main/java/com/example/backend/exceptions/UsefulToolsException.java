package com.example.backend.exceptions;

import java.io.Serial;

public class UsefulToolsException extends RuntimeException {
    public UsefulToolsException(String message) {
        super(message);
    }

    @Serial
    private static final long serialVersionUID = -7044616904994766398L;
}
