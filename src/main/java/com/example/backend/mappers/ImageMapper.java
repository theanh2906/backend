package com.example.backend.mappers;

import com.example.backend.dtos.ImageDto;
import com.example.backend.models.Images;
import com.example.backend.utils.Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class ImageMapper {
    public static ImageDto toDto(Images entity) {
        com.example.backend.dtos.ImageDto dto = new com.example.backend.dtos.ImageDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUrl(Utils.createBase64Image(entity.getData()));
        return dto;
    }

    public static Images toEntity(MultipartFile file) {
        try {
            final Images entity = new Images();
            entity.setName(file.getOriginalFilename());
            entity.setData(file.getBytes());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setImageType(Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase());
            entity.setSize(file.getSize());
            return entity;
        } catch (IOException e) {
            return null;
        }
    }
}
