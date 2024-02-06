package com.example.backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public abstract class BaseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -3264973007689504090L;
    private String id;
    private Long createdDate;
    private Long lastModifiedDate;
}
