package com.kamilglazer.gosport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String authorFirstName;
    private String authorLastName;
    private String authorProfileImage;
    private Long authorId;

    public String getAuthorProfileImage() {
        return authorProfileImage !=null ? "http://localhost:8080/uploads/" + authorProfileImage : null;
    }
}