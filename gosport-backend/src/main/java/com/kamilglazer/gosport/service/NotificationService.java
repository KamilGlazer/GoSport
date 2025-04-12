package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.response.NotificationResponse;

import java.util.List;
import java.util.Map;

public interface NotificationService {
    Map<String, Integer> countUnReadNotifications(String token);
    List<NotificationResponse> getAllNotifications(String token);
    Void allRead(String token);
}
