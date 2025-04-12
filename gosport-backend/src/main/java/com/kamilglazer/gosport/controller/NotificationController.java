package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.NotificationResponse;
import com.kamilglazer.gosport.model.Notification;
import com.kamilglazer.gosport.service.NotificationService;
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

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String,Integer>> countUnReadNotifications(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(notificationService.countUnReadNotifications(token));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(notificationService.getAllNotifications(token));
    }

    @PatchMapping
    public ResponseEntity<Void> allRead(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(notificationService.allRead(token));
    }

}
