package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileDto {
    private String name;
    private String extension;
    private Long uploadedDate;
}
