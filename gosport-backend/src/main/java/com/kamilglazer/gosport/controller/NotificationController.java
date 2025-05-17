package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.NotificationResponse;
import com.kamilglazer.gosport.model.Notification;
import com.kamilglazer.gosport.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final JwtService jwtService;
    private final NotificationService notificationService;

    @Operation(
            summary = "Count unread notifications",
            description = "Returns the number of unread notifications for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unread notifications count returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String,Integer>> countUnReadNotifications(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(notificationService.countUnReadNotifications(token));
    }

    @Operation(
            summary = "Get all notifications",
            description = "Retrieves all notifications for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(notificationService.getAllNotifications(token));
    }

    @Operation(
            summary = "Mark all notifications as read",
            description = "Marks all notifications as read for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications marked as read"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PatchMapping
    public ResponseEntity<Void> allRead(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(notificationService.allRead(token));
    }

}
