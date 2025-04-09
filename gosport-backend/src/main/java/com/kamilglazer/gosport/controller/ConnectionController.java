package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/connection")
@RequiredArgsConstructor
public class ConnectionController {

    private final JwtService jwtService;
    private final ConnectionService connectionService;

    @PostMapping("/{id}")
    public ResponseEntity<Void> createConnection(@PathVariable Long id, @RequestHeader("Authorization") String authHeader){
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(connectionService.createConnection(id,token));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateConnection(@PathVariable Long id, @RequestHeader("Authorization") String authHeader, @RequestParam String action){
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(connectionService.updateConnection(id,token,action));
    }
}
