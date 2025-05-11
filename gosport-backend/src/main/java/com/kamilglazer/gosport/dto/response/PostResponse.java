package com.kamilglazer.gosport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String authorFirstName;
    private String authorLastName;
    private String authorProfileImage;
    private int likeCount;
    private Long authorId;
    private boolean likedByMe;


    public String getAuthorProfileImage() {
        return authorProfileImage !=null ? "http://localhost:8080/uploads/" + authorProfileImage : null;
    }

}

