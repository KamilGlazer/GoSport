package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.service.ConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/connection")
@RequiredArgsConstructor
public class ConnectionController {

    private final JwtService jwtService;
    private final ConnectionService connectionService;

    @Operation(summary = "Send a connection request", description = "Sends a connection request to another user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection request sent successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping("/{id}")
    public ResponseEntity<Void> createConnection(@PathVariable Long id, @RequestHeader("Authorization") String authHeader){
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(connectionService.createConnection(id,token));
    }

    @Operation(summary = "Update connection status", description = "Accepts or rejects a connection request by user ID using an action parameter (accept/reject).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection updated successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Connection not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid action", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateConnection(@PathVariable Long id, @RequestHeader("Authorization") String authHeader, @RequestParam String action){
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(connectionService.updateConnection(id,token,action));
    }
}
