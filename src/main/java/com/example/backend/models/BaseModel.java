package com.example.backend.models;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 697716261171264405L;
    private Long createdDate;
    private Long lastModifiedDate;
}
