package com.example.backend.configurations;

import com.example.backend.models.Greeting;
import com.example.backend.models.UserModel;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class Hello implements Function<UserModel, Greeting> {

    @Override
    public Greeting apply(UserModel user) {
        return new Greeting("Hello, " + user.getName() + "!\n");
    }
}