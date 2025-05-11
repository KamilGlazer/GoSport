package com.kamilglazer.gosport.model;

import com.kamilglazer.gosport.domain.NOTIFICATION_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "maker_id", nullable = false)
    private User makerUser;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private NOTIFICATION_TYPE type;

    private String message;
    private boolean isRead = false;
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
