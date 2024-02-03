package com.example.backend.rest;

import com.example.backend.dtos.ResponseDto;
import com.example.backend.dtos.UserDto;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.BarcodeService;
import com.example.backend.shared.Constant;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/all")
    public String allAccess() {
        return "Public access";
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return repository.findAll();
    }

    @GetMapping("/header")
    public String getHeader(HttpServletRequest request) {
        return request.getHeader("iv-user");
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @PostMapping("/send-sqs-message")
    public void sendSQSMessage(@RequestBody String message) {
        messagingTemplate.convertAndSend(Constant.AWSConfig.AWS_SQS_QUEUE_NAME, message);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> userAccess() {
        try {
            return ResponseEntity.ok(new ResponseDto<>(true, "Approved"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(false, e.getLocalizedMessage()));
        }
    }
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserRepository repository;
    @Autowired
    private BarcodeService barcodeService;
    @Autowired
    private QueueMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("all-users")
    public ResponseEntity<?> allUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
