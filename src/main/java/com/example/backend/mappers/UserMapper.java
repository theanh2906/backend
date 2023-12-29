package com.example.backend.mappers;

import com.example.backend.dtos.SignupRequest;
import com.example.backend.dtos.UserDto;
import com.example.backend.models.User;
import com.example.backend.services.UserDetailsImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

public class UserMapper {
    public static User toDto(SignupRequest dto) {
        final User model = new User();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    public static UserDto toDto(UserDetailsImpl userDetails) {
        return UserDto
                .builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .id(userDetails.getId())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();
    }
}
