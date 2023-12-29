package com.example.backend.dtos.fshare;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FileFolderInfo {
    private String id;
    @JsonProperty("linkcode")
    private String linkCode;
    private String name;
    private String secure;
    @JsonProperty("directlink")
    private String directLink;
    private String type;
    private String path;
    private String size;
    @JsonProperty("downloadcount")
    private String downloadCount;
    @JsonProperty("mimetype")
    private String mimeType;
    private String created;
    @JsonProperty("pwd")
    private String password;
    @JsonProperty("allow_follow")
    private String allowFollow;
    @JsonProperty("num_follower")
    private String numFollower;
    private List<FileFolderInfo> children;
}
