package com.kamilglazer.Zentro.service;

import com.kamilglazer.Zentro.dto.request.LoginRequest;
import com.kamilglazer.Zentro.dto.request.RegisterRequest;
import com.kamilglazer.Zentro.dto.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    JwtResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
    ResponseEntity<?> validateToken(String token);
}
