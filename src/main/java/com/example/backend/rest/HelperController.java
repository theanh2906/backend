package com.example.backend.rest;

import com.example.backend.dtos.LoginRequest;
import com.example.backend.dtos.ResponseDto;
import com.example.backend.services.FileService;
import com.example.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/helpers")
public class HelperController {
    @Autowired
    private FileService fileService;

    @PostMapping("/encode-login")
    public String encodeLogin(@RequestBody LoginRequest loginRequest) {
        String json = Utils.stringifyJson(loginRequest);
        return Utils.encodeBase64Str(json);
    }

    @PostMapping("/encode")
    public ResponseEntity<ResponseDto<String>> encode(@RequestBody String string) {
        String encodedStr;
        try {
            encodedStr = Utils.encodeBase64Str(string);
            return ResponseEntity.ok(new ResponseDto<>(true, encodedStr));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(false, e.getLocalizedMessage()));
        }
    }

    @PostMapping("/generate-json")
    public ResponseEntity<?> generateJson(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(fileService.parseJson(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(false, e.getLocalizedMessage()));
        }
    }
}
