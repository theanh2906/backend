package com.example.backend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventDto {
    private String id;
    private Boolean allDay;
    private String start;
    private String end;
    private String title;
    private String userId;
}
