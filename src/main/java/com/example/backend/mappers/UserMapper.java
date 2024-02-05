package com.example.backend.mappers;

import com.example.backend.dtos.FirebaseUser;
import com.example.backend.dtos.SignupRequest;
import com.example.backend.dtos.UserDto;
import com.example.backend.models.Role;
import com.example.backend.models.RoleEnum;
import com.example.backend.models.User;
import com.example.backend.services.UserDetailsImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toModel(SignupRequest dto) {
        final User model = new User();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    public static UserDto toModel(UserDetailsImpl userDetails) {
        return UserDto
                .builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .id(userDetails.getId())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();
    }

    public static User toModel(FirebaseUser registedUser) {
        User user = new User();
        user.setId(registedUser.getLocalId());
        user.setEmail(registedUser.getEmail());
        user.setUsername(registedUser.getEmail());
        Set<Role> roles = new HashSet<>();
        user.setRoles(roles);
        return user;
    }
}
