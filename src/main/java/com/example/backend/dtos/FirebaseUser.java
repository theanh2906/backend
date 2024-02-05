package com.example.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirebaseUser {
    private String kind;
    private String idToken;
    private String email;
    private String refreshToken;
    private String expiresIn;
    private String localId;
    private Boolean registered;
}
