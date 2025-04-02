package com.kamilglazer.gosport.model;

import java.time.LocalDateTime;
import com.kamilglazer.gosport.domain.NOTIFICATION_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private NOTIFICATION_TYPE type;

    private String message;
    private boolean isRead;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
