package com.example.backend.rest;

import com.example.backend.dtos.ResponseDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {
        FshareController.class,
        EventController.class,
        GoogleController.class,
        NoteController.class,
        RoleController.class,
        SocketController.class,
        EventController.class
})
public class ExceptionHandlerController {
    @ExceptionHandler(Exception.class)
    public ResponseDto<?> handleException(Exception e) {
        return new ResponseDto<>(false, e.getMessage());
    }
}
