package com.example.backend.rest;

import com.example.backend.services.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/web-crawler")
public class WebCrawlerController {
    @Autowired
    private WebCrawlerService webCrawlerService;

    @GetMapping("/download-images")
    public ResponseEntity<?> downloadImages(@RequestParam String url, @RequestParam String selector, final HttpServletResponse response) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("url", url);
            params.put("selector", selector);
            webCrawlerService.downloadImages(params, response);
            return ResponseEntity.ok("Downloading...");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/save-images")
    public void saveImages(@RequestParam String url, @RequestParam String selector, @RequestParam(required = false) String imageAttribute) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("url", url);
            params.put("selector", selector);
            params.put("imageAttribute", imageAttribute);
            Thread thread = new Thread(() -> webCrawlerService.saveImages(params));
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/get-links")
    public List<String> getLinks(@RequestParam String url,
                                 @RequestParam Integer size,
                                 @RequestParam String selector,
                                 @RequestParam String imageSelector,
                                 @RequestParam String imageAttribute) {
        List<String> results = new ArrayList<>();
        try {
            results = webCrawlerService.getLinks(url, size, selector, imageSelector, imageAttribute);
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @GetMapping("")
    public List<String> getText(@RequestParam String url, @RequestParam Integer size, @RequestParam String selector) {
        List<String> results = new ArrayList<>();
        try {
            results = webCrawlerService.getText(url, size, selector);
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
