package com.example.backend.mappers;

import com.example.backend.dtos.EventDto;
import com.example.backend.models.Event;
import com.example.backend.utils.Utils;
import org.springframework.beans.BeanUtils;

public class EventMapper {
    public static Event toModel(EventDto dto) {
        final Event model = new Event();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    public static EventDto toDto(Event model) {
        final EventDto dto = Utils.getInstance(EventDto.class);
        BeanUtils.copyProperties(model, dto);
        return dto;
    }
}
