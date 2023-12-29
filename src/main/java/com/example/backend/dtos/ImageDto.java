package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ImageDto extends BaseDto {
    @Serial
    private static final long serialVersionUID = -5167744428844541368L;
    private String id;
    private String name;
    private String url;
}
