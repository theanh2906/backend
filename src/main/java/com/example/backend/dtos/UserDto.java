package com.example.backend.dtos;

import com.example.backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    public User toModel() {
        final User model = new User();
        model.setId(this.id);
        model.setUsername(this.username);
        model.setEmail(this.email);
        return model;
    }
    private String id;
    private String username;
    private String email;
    private List<String> roles;

    static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    static class UserDtoBuilder {
        UserDtoBuilder()  {

        }

        public UserDto build() {
            return new UserDto(id, username, email, roles);
        }

        public UserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public UserDtoBuilder username(String username) {
            this.username = username;
            return this;
        }
        private String id;
        private String username;
        private String email;
        private List<String> roles;
    }
}
