package com.kamilglazer.gosport.service.impl;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import com.kamilglazer.gosport.dto.request.MessageRequest;
import com.kamilglazer.gosport.dto.response.FriendResponse;
import com.kamilglazer.gosport.dto.response.MessageResponse;
import com.kamilglazer.gosport.exception.UserNotFoundException;
import com.kamilglazer.gosport.model.Connection;
import com.kamilglazer.gosport.model.Message;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.repository.ConnectionRepository;
import com.kamilglazer.gosport.repository.MessageRepository;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final JwtService jwtService;

    @Override
    public List<FriendResponse> getAllConnected(String token) {
        String email = jwtService.extractUsername(token);
        User loggedUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Connection> connections = connectionRepository.findAllBySenderOrReceiverAndStatus(
                loggedUser, loggedUser, CONNECTION_STATUS.ACCEPTED
        );

        return connections.stream()
                .map(conn -> conn.getSender().equals(loggedUser) ? conn.getReceiver() : conn.getSender())
                .distinct()
                .map(user -> FriendResponse.builder()
                        .userId(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .profileImage(user.getCredentials().getProfileImage())
                        .build())
                .toList();
    }


    @Override
    public MessageResponse sendMessage(String token, MessageRequest request) {
        String email = jwtService.extractUsername(token);
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .build();

        messageRepository.save(message);

        return MessageResponse.builder()
                .content(message.getContent())
                .sendAt(message.getSendAt())
                .isOwn(true)
                .build();
    }

    @Override
    public List<MessageResponse> getMessagesWith(String token, Long userId) {
        String email = jwtService.extractUsername(token);
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Other user not found"));

        List<Message> messages = messageRepository.findConversationBetween(currentUser, otherUser);

        return messages.stream()
                .map(m -> MessageResponse.builder()
                        .content(m.getContent())
                        .sendAt(m.getSendAt())
                        .isOwn(m.getSender().getId().equals(currentUser.getId()))
                        .build())
                .toList();
    }


}
