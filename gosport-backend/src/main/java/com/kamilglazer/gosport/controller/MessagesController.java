package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.request.MessageRequest;
import com.kamilglazer.gosport.dto.response.FriendResponse;
import com.kamilglazer.gosport.dto.response.MessageResponse;
import com.kamilglazer.gosport.dto.response.UserProfileResponse;
import com.kamilglazer.gosport.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all connected friends", description = "Retrieves a list of users with whom the current user has an accepted connection.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connected users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/getConnected")
    public ResponseEntity<List<FriendResponse>> getAllConnected(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(messageService.getAllConnected(token));
    }

    @Operation(summary = "Send a message", description = "Sends a new message to another user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Recipient not found", content = @Content)
    })
    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MessageRequest request) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(messageService.sendMessage(token, request));
    }

    @Operation(summary = "Get messages with a user", description = "Fetches the conversation (message list) with the specified user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User or messages not found", content = @Content)
    })
    @GetMapping("/with/{userId}")
    public ResponseEntity<List<MessageResponse>> getMessagesWith(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long userId) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(messageService.getMessagesWith(token, userId));
    }

}
