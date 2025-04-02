package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.request.LoginRequest;
import com.kamilglazer.gosport.dto.request.RegisterRequest;
import com.kamilglazer.gosport.dto.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    JwtResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
    ResponseEntity<?> validateToken(String token);
}
