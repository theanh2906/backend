package com.example.backend.dtos.fshare;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FshareLoginRequest {
    @JsonProperty("user_email")
    private String email;
    private String password;
    @JsonProperty("app_key")
    private String appKey;
}
