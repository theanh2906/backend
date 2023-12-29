package com.example.backend.dtos.fshare;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInfoResponse {
    private String id;
    private String level;
    private String name;
    private String phone;
    private String birthday;
    private String gender;
    private String address;
    @JsonProperty("id_card")
    private String idCard;
    private String city;
    private String email;
    @JsonProperty("joindate")
    private String joinDate;
    @JsonProperty("totalpoints")
    private String totalPoints;
    @JsonProperty("expire_vip")
    private String expireVip;
    private String traffic;
    @JsonProperty("traffic_used")
    private String trafficUsed;
    private String webspace;
    @JsonProperty("webspace_used")
    private String webspaceUsed;
    @JsonProperty("webspace_secure")
    private String webspaceSecure;
    @JsonProperty("webspace_secure_used")
    private String webspaceSecureUsed;
    private String amount;
    @JsonProperty("dl_time_avail")
    private String dlTimeAvailable;
    @JsonProperty("account_type")
    private String accountType;
}
