package com.example.backend.rest;

import com.example.backend.dtos.fshare.FileFolderInfo;
import com.example.backend.dtos.fshare.FshareLogoutResponse;
import com.example.backend.dtos.fshare.LoginResponse;
import com.example.backend.dtos.fshare.UploadResponse;
import com.example.backend.dtos.fshare.UserInfoResponse;
import com.example.backend.services.FshareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@RestController
@RequestMapping("/api/fshare")
public class FshareController {
    @Autowired
    private FshareService fshareService;

    @PostMapping("/login")
    Mono<LoginResponse> login() {
        return fshareService.login();
    }

    @GetMapping("/logout")
    Mono<FshareLogoutResponse> logout(@RequestParam("sessionId") String sessionId, @RequestParam("token") String token) {
        return fshareService.logout(sessionId, token);
    }

    @GetMapping("/user-info")
    Mono<UserInfoResponse> getUserInfo() {
        return fshareService.getUserInfo();
    }

    @PostMapping("/upload")
    Mono<UploadResponse> upload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        return fshareService.upload(file, path).doOnError(Throwable::printStackTrace);
    }

    @GetMapping("/folders")
    Flux<FileFolderInfo> getAllFolders() {
        return fshareService
                .getFolders()
                .sort(Comparator.comparing(FileFolderInfo::getType));
    }
}
