package com.onlinecinema.controller;

import com.onlinecinema.dto.AuthResponseDto;
import com.onlinecinema.dto.LoginRequest;
import com.onlinecinema.dto.RegisterRequest;
import com.onlinecinema.dto.UserDto;
import com.onlinecinema.security.SecurityUtils;
import com.onlinecinema.service.AuthService;
import com.onlinecinema.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Value("${google.oauth.client-id:}")
    private String googleClientId;

    @Value("${google.oauth.redirect-uri:http://localhost:3000/auth/google/callback}")
    private String googleRedirectUri;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequest request) {
        UserDto user = userService.register(request);
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());
        
        AuthResponseDto authResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequest request) {
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Long userId = securityUtils.getCurrentUserId();
        UserDto userDto = userService.findById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/google/url")
    public ResponseEntity<Map<String, String>> getGoogleAuthUrl() {
        Map<String, String> response = new HashMap<>();
        
        if (googleClientId == null || googleClientId.isEmpty()) {
            response.put("error", "Google OAuth not configured");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        String scope = URLEncoder.encode("openid email profile", StandardCharsets.UTF_8);
        String redirectUri = URLEncoder.encode(googleRedirectUri, StandardCharsets.UTF_8);
        String state = URLEncoder.encode("state_token_here", StandardCharsets.UTF_8);
        
        String authUrl = String.format(
            "https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s&access_type=offline&prompt=consent",
            googleClientId, redirectUri, scope, state
        );
        
        response.put("url", authUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<Map<String, String>> googleCallback(@RequestParam(required = false) String code,
                                                               @RequestParam(required = false) String error) {
        Map<String, String> response = new HashMap<>();
        
        if (error != null) {
            response.put("error", error);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        if (code == null) {
            response.put("error", "Authorization code not provided");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        response.put("message", "Google OAuth callback received. Code: " + code);
        response.put("note", "This is a simplified implementation. In production, exchange code for tokens and create/login user.");
        
        return ResponseEntity.ok(response);
    }
}
