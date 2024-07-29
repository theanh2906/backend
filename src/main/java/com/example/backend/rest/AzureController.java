package com.example.backend.rest;

import com.example.backend.shared.Constant;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/azure")
public class AzureController {
    @GetMapping("/sso")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect(String.format("https://login.microsoftonline.com/%s/oauth2/v2.0/authorize?client_id=%s&response_type=token&redirect_uri=%s&scope=.default", Constant.Azure.TENANT_ID, Constant.Azure.CLIENT_ID, Constant.Azure.REDIRECT_URI));
    }
    @GetMapping("/callback/{token}")
    public ResponseEntity<?> callback(@PathVariable String token) {
        return ResponseEntity.ok(token);
    }
}
