package com.example.backend.services;

import com.example.backend.dtos.fshare.DownloadLinkResponse;
import com.example.backend.dtos.fshare.FileFolderInfo;
import com.example.backend.dtos.fshare.FshareLoginRequest;
import com.example.backend.dtos.fshare.FshareLogoutResponse;
import com.example.backend.dtos.fshare.LoginResponse;
import com.example.backend.dtos.fshare.UploadRequest;
import com.example.backend.dtos.fshare.UploadResponse;
import com.example.backend.dtos.fshare.UserInfoResponse;
import com.example.backend.shared.Constant;
import com.example.backend.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class FshareService {
    private final WebClient webClient = WebClient.create();
    protected String sessionId;
    protected String token;

    public Flux<FileFolderInfo> getFolders() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", 0);
        params.put("path", "");
        params.put("dirOnly", 0);
        return webClient
                .get()
                .uri(Objects.requireNonNull(Utils.buildUriWithParams(Constant.Fshare.FILE_FOLDER_INFO_URL, params)))
                .header(Constant.HeaderAttribute.USER_AGENT, Constant.Fshare.USER_AGENT)
                .cookie(Constant.HeaderAttribute.SESSION_ID, this.sessionId)
                .retrieve()
                .bodyToFlux(FileFolderInfo.class)
                .flatMap(fileFolderInfo -> {
                    if (fileFolderInfo.getType().equals(Constant.Fshare.FolderType.FOLDER.getValue())) {
                        return getSubFolder(fileFolderInfo).collectList().mapNotNull(subFolder -> {
                            if (fileFolderInfo.getType().equals(Constant.Fshare.FolderType.FOLDER.getValue())) {
                                fileFolderInfo.setChildren(subFolder);
                            }
                            return fileFolderInfo;
                        });
                    } else {
                        return Mono.just(fileFolderInfo);
                    }
                });
    }

    private Flux<FileFolderInfo> getSubFolder(FileFolderInfo fileFolder) {
        if (fileFolder.getType().equals(Constant.Fshare.FolderType.FOLDER.getValue())) {
            Map<String, Object> params = new HashMap<>();
            params.put("pageIndex", 0);
            params.put("path", removeFirstSlash(fileFolder.getPath() + "/" + fileFolder.getName()));
            params.put("dirOnly", 0);
            return webClient
                    .get()
                    .uri(Objects.requireNonNull(Utils.buildUriWithParams(Constant.Fshare.FILE_FOLDER_INFO_URL, params)))
                    .header(Constant.HeaderAttribute.USER_AGENT, Constant.Fshare.USER_AGENT)
                    .cookie(Constant.HeaderAttribute.SESSION_ID, this.sessionId)
                    .retrieve()
                    .bodyToFlux(FileFolderInfo.class)
                    .flatMap(fileFolderInfo -> getSubFolder(fileFolderInfo).collectList().mapNotNull(subFolder -> {
                        if (fileFolderInfo.getType().equals(Constant.Fshare.FolderType.FOLDER.getValue())) {
                            fileFolderInfo.setChildren(subFolder);
                        }
                        return fileFolderInfo;
                    }));
        }
        return Flux.just(fileFolder);
    }

    public Mono<UserInfoResponse> getUserInfo() {
        return login().flatMap(loginResponse -> webClient
                .get()
                .uri(Constant.Fshare.USER_INFO_URL)
                .header(Constant.HeaderAttribute.USER_AGENT, Constant.Fshare.USER_AGENT)
                .cookie(Constant.HeaderAttribute.SESSION_ID, loginResponse.getSessionId())
                .retrieve()
                .bodyToMono(UserInfoResponse.class));
    }

    public Mono<LoginResponse> login() {
        return webClient
                .post()
                .uri(Constant.Fshare.LOGIN_URL)
                .header(Constant.HeaderAttribute.USER_AGENT, Constant.Fshare.USER_AGENT)
                .body(Mono.just(Constant.Fshare.LOGIN_REQUEST), FshareLoginRequest.class)
                .retrieve().bodyToMono(LoginResponse.class);
    }

    public Mono<FshareLogoutResponse> logout(String sessionId, String token) {
        return webClient
                .get()
                .uri(Constant.Fshare.LOGOUT_URL)
                .cookie(Constant.HeaderAttribute.SESSION_ID, sessionId)
                .retrieve()
                .bodyToMono(FshareLogoutResponse.class);
    }

    private String removeFirstSlash(String path) {
        if (path.startsWith("//")) {
            return path.substring(2);
        } else if (path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }

    public void setFshareData(HttpServletRequest request) {
        this.sessionId = request.getHeader("sessionId");
        this.token = request.getHeader("token");
    }

    public Mono<UploadResponse> upload(MultipartFile file, String filePath) {
        return createUploadRequest(file, filePath, this.token)
                .flatMap(uploadRequest ->
                        webClient
                                .post()
                                .uri(Constant.Fshare.UPLOAD_URL)
                                .header(Constant.HeaderAttribute.USER_AGENT, Constant.Fshare.USER_AGENT)
                                .cookie(Constant.HeaderAttribute.SESSION_ID, this.sessionId)
                                .body(Mono.just(uploadRequest), UploadRequest.class).retrieve()
                                .bodyToMono(DownloadLinkResponse.class))
                .flatMap(downloadLink -> {
                    try {
                        return webClient
                                .post()
                                .uri(downloadLink.getLocation())
                                .header(Constant.HeaderAttribute.USER_AGENT, Constant.Fshare.USER_AGENT)
                                .accept(MediaType.ALL)
                                .body(Mono.just(file.getBytes()), byte[].class)
                                .retrieve()
                                .bodyToMono(String.class)
                                .mapNotNull(res -> Utils.map(UploadResponse.class, res));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Mono.error(e);
                    }
                });
    }

    private Mono<UploadRequest> createUploadRequest(MultipartFile file, String filePath, String token) {
        final UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setName(file.getOriginalFilename());
        uploadRequest.setSecured(1);
        uploadRequest.setPath(filePath);
        uploadRequest.setSize(String.valueOf(file.getSize()));
        uploadRequest.setToken(token);
        return Mono.just(uploadRequest);
    }
}
