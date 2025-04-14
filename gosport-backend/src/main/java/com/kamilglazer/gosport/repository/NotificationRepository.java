package com.kamilglazer.gosport.repository;

import com.kamilglazer.gosport.domain.NOTIFICATION_TYPE;
import com.kamilglazer.gosport.model.Notification;
import com.kamilglazer.gosport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    int countByUserAndIsReadFalse(User user);
    List<Notification> findAllByUserOrderByIsReadAscCreatedAtDesc(User user);
    Optional<Notification> findByUserAndMakerUserAndType(User user, User makerUser, NOTIFICATION_TYPE type);
}
