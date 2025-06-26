package com.example.backend.rest;

import com.example.backend.dtos.ImageDto;
import com.example.backend.dtos.ResponseDto;
import com.example.backend.services.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
    @DeleteMapping("/images")
    public ResponseEntity<?> deleteAllImages() {
        return ResponseEntity.ok().body(storageService.deleteAllImages());
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteFiles(@RequestBody List<String> paths) throws IOException {
        for (String path : paths) {
            Path filePath = Paths.get(path);
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                Files.delete(filePath);
            }
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable String id) {
        storageService.deleteImage(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String path) {
        Resource file = new FileSystemResource(new File(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(path)))
                .header("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/download-image")
    public ResponseEntity<?> downloadImages(@RequestParam int index, final HttpServletResponse response) {
        storageService.downloadImage(index, response);
        return new ResponseEntity<>("Downloading...", HttpStatus.ACCEPTED);
    }

    @GetMapping("/files")
    public ResponseEntity<?> getAllFiles() {
        return ResponseEntity.ok().body(storageService.getAllFiles());
    }

    @GetMapping("/image-base64")
    public ResponseEntity<?> getImageBase64(@RequestParam int index) {
        return new ResponseEntity<>(storageService.getImageBase64(index), HttpStatus.ACCEPTED);
    }

    @GetMapping("/images")
    public ResponseEntity<?> getImages() {
        List<ImageDto> result = storageService.getAllImages();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile[] file) {
        Arrays.stream(file).forEach(storageService::save);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload-images")
    public ResponseEntity<?> uploadImage(@RequestParam("file") List<MultipartFile> file) {
        return ResponseEntity.ok().body(storageService.uploadImages(file));
    }
    @Autowired
    private StorageService storageService;
}
