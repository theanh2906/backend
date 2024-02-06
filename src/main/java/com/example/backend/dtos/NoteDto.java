package com.example.backend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class NoteDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5726652802489429570L;
    private String id;
    private String content;
    private String title;
    private Object createdDate;
}
