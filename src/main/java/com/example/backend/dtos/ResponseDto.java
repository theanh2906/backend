package com.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ResponseDto<T> {
    private Boolean success;
    private List<String> messages;
    private String message;
    private T data;

    public ResponseDto(T data) {
        if (data instanceof Boolean) {
            this.success = (Boolean) data;
        } else {
            this.data = data;
        }
    }

    public ResponseDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseDto(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ResponseDto(String message) {
        this.message = message;
    }

    public ResponseDto(Boolean success, List<String> messages) {
        this.success = success;
        this.messages = messages;
    }
}
