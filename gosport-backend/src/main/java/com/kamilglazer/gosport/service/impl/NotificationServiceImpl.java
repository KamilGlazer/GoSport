package com.kamilglazer.gosport.service.impl;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.NotificationResponse;
import com.kamilglazer.gosport.exception.*;
import com.kamilglazer.gosport.model.Notification;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.repository.NotificationRepository;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public Map<String, Integer> countUnReadNotifications(String token) {
        String email = jwtService.extractUsername(token);
        User loggedUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        return Map.of("unreadCount", notificationRepository.countByUserAndIsReadFalse(loggedUser));
    }


    @Override
    public List<NotificationResponse> getAllNotifications(String token) {
        String email = jwtService.extractUsername(token);
        User loggedUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Notification> notifications = notificationRepository.findAllByUserOrderByIsReadAscCreatedAtDesc(loggedUser);

        return notifications.stream().map(notification -> NotificationResponse.builder()
                .firstName(notification.getMakerUser().getFirstName())
                .lastName(notification.getMakerUser().getLastName())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .message(notification.getMessage())
                .read(notification.isRead())
                .profileImage(notification.getMakerUser().getCredentials().getProfileImage())
                .build()).collect(Collectors.toList());
    }

    @Override
    public Void allRead(String token) {
        String email = jwtService.extractUsername(token);
        User loggedUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Notification> notifications = notificationRepository.findAllByUserOrderByIsReadAscCreatedAtDesc(loggedUser);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
        return null;
    }
}
