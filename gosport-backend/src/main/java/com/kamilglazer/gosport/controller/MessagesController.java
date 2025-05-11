package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.request.MessageRequest;
import com.kamilglazer.gosport.dto.response.FriendResponse;
import com.kamilglazer.gosport.dto.response.MessageResponse;
import com.kamilglazer.gosport.dto.response.UserProfileResponse;
import com.kamilglazer.gosport.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessagesController {

    private final MessageService messageService;
    private final JwtService jwtService;

    @GetMapping("/getConnected")
    public ResponseEntity<List<FriendResponse>> getAllConnected(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(messageService.getAllConnected(token));
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MessageRequest request) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(messageService.sendMessage(token, request));
    }

    @GetMapping("/with/{userId}")
    public ResponseEntity<List<MessageResponse>> getMessagesWith(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long userId) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(messageService.getMessagesWith(token, userId));
    }

}
