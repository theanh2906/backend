package com.example.backend.mappers;

import com.example.backend.dtos.NoteDto;
import com.example.backend.models.Note;
import org.springframework.beans.BeanUtils;

public class NoteMapper {
    public static Note toModel(NoteDto dto) {
        final Note model = new Note();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    public static NoteDto toDto(Note model) {
        final NoteDto dto = new NoteDto();
        BeanUtils.copyProperties(model, dto);
        return dto;
    }
}
