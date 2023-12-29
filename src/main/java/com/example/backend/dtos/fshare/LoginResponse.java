package com.example.backend.dtos.fshare;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {
    private Integer code;
    @JsonProperty("msg")
    private String message;
    private String token;
    @JsonProperty("session_id")
    private String sessionId;
}
