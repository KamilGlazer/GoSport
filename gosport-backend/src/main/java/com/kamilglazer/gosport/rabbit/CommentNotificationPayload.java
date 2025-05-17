package com.kamilglazer.gosport.rabbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentNotificationPayload {
    private Long postId;
    private Long senderId;
    private Long receiverId;
    private String content;
}