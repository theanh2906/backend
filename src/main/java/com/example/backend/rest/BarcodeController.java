package com.example.backend.rest;

import com.example.backend.services.BarcodeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
public class BarcodeController {
    @Autowired
    private BarcodeService barcodeService;

    @GetMapping("/download")
    public ResponseEntity<?> downloadQRCode(@RequestParam String text, final HttpServletResponse response) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(barcodeService.generateQRCode(text), "png", baos);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=qr.jpg");
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
            IOUtils.copy(inputStream, response.getOutputStream());
            IOUtils.closeQuietly(response.getOutputStream());
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/image")
    public ResponseEntity<?> getBase64QRImage(@RequestParam String text) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(barcodeService.generateQRCode(text), "png", baos);
            Map<String, String> response = new HashMap<>();
            response.put("data", "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray()));
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
