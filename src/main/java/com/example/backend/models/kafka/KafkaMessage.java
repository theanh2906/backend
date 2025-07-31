package com.example.backend.models.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class KafkaMessage {
    private Integer timestamp;
    private String type;
    private LinkedHashMap<String, Object> data;
}
