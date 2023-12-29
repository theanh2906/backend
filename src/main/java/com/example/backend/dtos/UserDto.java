package com.example.backend.dtos;

import com.example.backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    public User toModel() {
        return User.builder()
                .id(this.id)
                .build();
    }

    private String id;
    private String username;
    private String email;
    private List<String> roles;
}
