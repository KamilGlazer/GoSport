package com.kamilglazer.gosport.repository;

import com.kamilglazer.gosport.model.Notification;
import com.kamilglazer.gosport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    int countByUserAndIsReadFalse(User user);
    List<Notification> findAllByUserOrderByIsReadAscCreatedAtDesc(User user);
}
