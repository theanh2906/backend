package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class NoteDto extends BaseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5726652802489429570L;
    private String content;
    private String title;
}
