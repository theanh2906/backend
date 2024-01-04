package com.example.backend.rest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageErrorController implements ErrorController {
    @GetMapping("/error")
    public String handleError() {
        return "forward:/index.html";
    }
}
