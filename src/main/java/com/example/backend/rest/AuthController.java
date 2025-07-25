package com.example.backend.rest;

import com.example.backend.dtos.FirebaseUser;
import com.example.backend.dtos.JwtResponse;
import com.example.backend.dtos.LoginRequest;
import com.example.backend.dtos.ResponseDto;
import com.example.backend.dtos.SignupRequest;
import com.example.backend.mappers.UserMapper;
import com.example.backend.models.Role;
import com.example.backend.models.RoleEnum;
import com.example.backend.models.User;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserDetailsImpl;
import com.example.backend.services.UserService;
import com.example.backend.utils.JwtUtils;
import com.example.backend.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("add-user")
    public ResponseEntity<?> addUserInDb(@RequestBody FirebaseUser user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    /**
     * Method to authenticate login request
     *
     * @param data - encoded Base64 String of minified json generated from LoginRequest object.
     */
    @Operation(summary = "Login and authenticate credential")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody String data) {
        LoginRequest loginRequest = Utils.map(data, LoginRequest.class);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtSecret(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails
                .getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity.ok().body(new JwtResponse(jwt));
    }

    /**
     * Method to signup login request
     *
     * @param - encoded Base64 String of minified json generated from LoginRequest object
     */
    @Operation(summary = "Signup new credential")
    @PostMapping("/signup")
    @Deprecated
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(false, "Username already exist!"));
        } else if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(false, "Email already exist!"));
        }

        User user = UserMapper.toModel(signupRequest);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleEnum.ROLE_USER).orElse(null));
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseDto<>(true, "Successfully signup user!"));
    }

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${app.jwtExpiration}")
    private Long tokenExpiration;
}
