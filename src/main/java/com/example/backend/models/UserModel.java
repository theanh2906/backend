package com.example.backend.models;

public class UserModel {
    private String name;

    public UserModel() {
    }

    public UserModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
