package com.kamilglazer.Zentro.controller;

import com.kamilglazer.Zentro.config.JwtService;
import com.kamilglazer.Zentro.dto.request.LoginRequest;
import com.kamilglazer.Zentro.dto.request.RegisterRequest;
import com.kamilglazer.Zentro.dto.response.JwtResponse;
import com.kamilglazer.Zentro.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> register(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return authenticationService.validateToken(token);
    }

}