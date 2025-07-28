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
        BarcodeController.class,
        StorageController.class,
        TestController.class,
        AzureController.class,
        WebCrawlerController.class,
        AuthController.class
})
public class ExceptionHandlerController {
    @ExceptionHandler(Exception.class)
    public ResponseDto<?> handleException(Exception e) {
        var response = new ResponseDto<>();
        response.setSuccess(false);
        response.setMessage(e.getMessage());
        response.setStatusCode(500);
        return response;
    }
}
