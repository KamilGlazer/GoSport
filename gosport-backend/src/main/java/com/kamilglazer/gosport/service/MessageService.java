package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.request.MessageRequest;
import com.kamilglazer.gosport.dto.response.FriendResponse;
import com.kamilglazer.gosport.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {
    List<FriendResponse> getAllConnected(String token);
    MessageResponse sendMessage(String token, MessageRequest request);
    List<MessageResponse> getMessagesWith(String token, Long userId);
}
