package com.kamilglazer.Zentro.service.impl;

import com.kamilglazer.Zentro.config.JwtService;
import com.kamilglazer.Zentro.dto.request.LoginRequest;
import com.kamilglazer.Zentro.dto.request.RegisterRequest;
import com.kamilglazer.Zentro.dto.response.JwtResponse;
import com.kamilglazer.Zentro.exception.UserNotFoundException;
import com.kamilglazer.Zentro.exception.UserWithThisEmailAlreadyExists;
import com.kamilglazer.Zentro.model.User;
import com.kamilglazer.Zentro.repository.UserRepository;
import com.kamilglazer.Zentro.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public JwtResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {throw new UserWithThisEmailAlreadyExists("User with this email already exists");});

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobile(request.getMobile())
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return JwtResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .message("Registration successful")
                .build();
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }catch (Exception e){
            throw new UserNotFoundException("Invalid email or password");
        }

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("Invalid email or password"));
        var jwtToken = jwtService.generateToken(user);
        return JwtResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .message("Login successful")
                .build();
    }

    @Override
    public ResponseEntity<?> validateToken(String token) {
        try{
            String username = jwtService.extractUsername(token);
            var user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

            if(!jwtService.isTokenValid(token,user)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
            return ResponseEntity.ok("Token is valid");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
