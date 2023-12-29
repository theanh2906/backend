package com.example.backend.dtos.fshare;

import lombok.Data;

@Data
public class UploadRequest {
    private String name;
    private String size;
    private String path;
    private String token;
    private Integer secured;
}
