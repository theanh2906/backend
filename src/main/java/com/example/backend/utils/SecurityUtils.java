package com.example.backend.utils;

import com.example.backend.dtos.UserDto;
import com.example.backend.mappers.UserMapper;
import com.example.backend.services.UserDetailsImpl;
import com.example.backend.shared.Constant;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static UserDto getCurrentUser() {
        if (isAnonymous()) {
            return UserMapper.toDto(UserDetailsImpl.build(Constant.ADMIN));
        }
        return UserMapper.toDto((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public static boolean isAnonymous() {
        return SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser";
    }
}
