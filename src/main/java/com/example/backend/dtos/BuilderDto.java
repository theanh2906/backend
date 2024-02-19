package com.example.backend.dtos;

import java.lang.reflect.InvocationTargetException;

public abstract class BuilderDto<T extends BaseDto> {
    public T getInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return dto.getDeclaredConstructor(dto).newInstance();
    }
    Class<T> dto;
}
