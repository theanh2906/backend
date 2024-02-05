package com.example.backend.services;

import com.example.backend.dtos.FirebaseUser;
import com.example.backend.dtos.UserDto;
import com.example.backend.mappers.UserMapper;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.shared.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    public UserDto addUser(FirebaseUser user) {
        User addedUser = UserMapper.toModel(user);
        addedUser.setRoles(Collections.singleton(roleService.findById(Constant.Role.USER)));
        User result = userRepository.save(addedUser);
        return UserDto
                .builder()
                .id(addedUser.getId())
                .email(addedUser.getEmail())
                .username(addedUser.getUsername())
                .build();
    }
}
