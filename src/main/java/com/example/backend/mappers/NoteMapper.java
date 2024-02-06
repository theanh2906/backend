package com.example.backend.mappers;

import com.example.backend.dtos.NoteDto;
import com.example.backend.models.Note;
import org.springframework.beans.BeanUtils;

public class NoteMapper {
    public static Note toModel(NoteDto dto) {
        final Note model = new Note();
        BeanUtils.copyProperties(dto, model);
        Object createdDate = dto.getCreatedDate();
        if (dto.getCreatedDate() instanceof Long) {
            model.setCreatedDate((Long) dto.getCreatedDate());
        } else {
            model.setCreatedDate(((Integer) dto.getCreatedDate()).longValue());
        }
        return model;
    }

    public static NoteDto toDto(Note model) {
        final NoteDto dto = NoteDto.builder().build();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }
}
