package com.example.backend.dtos.fshare;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FshareLogoutResponse {
    private Integer code;
    @JsonProperty("msg")
    private String message;
}
