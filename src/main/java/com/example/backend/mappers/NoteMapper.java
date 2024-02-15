package com.example.backend.mappers;

import com.example.backend.dtos.NoteDto;
import com.example.backend.models.Note;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class NoteMapper {
    public static Note toModel(NoteDto dto) {
        final Note model = new Note();
        BeanUtils.copyProperties(dto, model);
        Object createdDate = dto.getCreatedDate();
        Object modifiedDate = dto.getModifiedDate();
        if (createdDate == null) {
            model.setCreatedDate(new Date().getTime());
        } else {
            if (createdDate instanceof Long) {
                model.setCreatedDate((Long) createdDate);
            } else {
                model.setCreatedDate(((Integer) createdDate).longValue());
            }
        }
        if (modifiedDate != null){
            if (modifiedDate instanceof Long) {
                model.setLastModifiedDate((Long) modifiedDate);
            } else {
                model.setLastModifiedDate(((Integer) modifiedDate).longValue());
            }
        } else {
            model.setLastModifiedDate(null);
        }
        return model;
    }

    public static NoteDto toDto(Note model) {
        final NoteDto dto = NoteDto.builder().build();
        BeanUtils.copyProperties(model, dto);
        dto.setModifiedDate(model.getLastModifiedDate());
        return dto;
    }
}
