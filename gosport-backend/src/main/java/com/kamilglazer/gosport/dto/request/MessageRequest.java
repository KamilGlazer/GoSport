package com.kamilglazer.gosport.dto.request;

import lombok.Data;

@Data
public class MessageRequest {
    private Long receiverId;
    private String content;
}

