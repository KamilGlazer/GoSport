package com.kamilglazer.gosport.dto.response;

import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import com.kamilglazer.gosport.domain.NOTIFICATION_TYPE;
import com.kamilglazer.gosport.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long userId;
    private NOTIFICATION_TYPE type;
    private String message;
    private String firstName;
    private String lastName;
    private boolean read;
    private String profileImage;
    private LocalDateTime createdAt;

    public String getProfileImage() {
        return profileImage !=null ? "http://localhost:8080/uploads/" + profileImage : null;
    }
}
