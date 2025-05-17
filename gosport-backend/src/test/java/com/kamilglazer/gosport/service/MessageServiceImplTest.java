package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import com.kamilglazer.gosport.dto.request.MessageRequest;
import com.kamilglazer.gosport.dto.response.FriendResponse;
import com.kamilglazer.gosport.dto.response.MessageResponse;
import com.kamilglazer.gosport.exception.UserNotFoundException;
import com.kamilglazer.gosport.model.Connection;
import com.kamilglazer.gosport.model.Message;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.model.UserCredentials;
import com.kamilglazer.gosport.repository.ConnectionRepository;
import com.kamilglazer.gosport.repository.MessageRepository;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageServiceImplTest {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ConnectionRepository connectionRepository;
    private JwtService jwtService;
    private MessageServiceImpl messageService;

    private final String token = "test.token";
    private final String email = "john.doe@example.com";

    private User getUser(Long id, String firstName, String lastName) {
        UserCredentials credentials = new UserCredentials();
        credentials.setProfileImage("img.png");
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com")
                .credentials(credentials)
                .build();
    }

    @BeforeEach
    void setUp() {
        messageRepository = mock(MessageRepository.class);
        userRepository = mock(UserRepository.class);
        connectionRepository = mock(ConnectionRepository.class);
        jwtService = mock(JwtService.class);
        messageService = new MessageServiceImpl(messageRepository, userRepository, connectionRepository, jwtService);
    }

    @Test
    void shouldReturnAllConnectedFriends() {
        User user = getUser(1L, "John", "Doe");
        User friend1 = getUser(2L, "Anna", "Nowak");
        User friend2 = getUser(3L, "Piotr", "Kowalski");

        Connection c1 = Connection.builder().sender(user).receiver(friend1).status(CONNECTION_STATUS.ACCEPTED).build();
        Connection c2 = Connection.builder().sender(friend2).receiver(user).status(CONNECTION_STATUS.ACCEPTED).build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(connectionRepository.findAllBySenderOrReceiverAndStatus(user, user, CONNECTION_STATUS.ACCEPTED))
                .thenReturn(List.of(c1, c2));

        List<FriendResponse> result = messageService.getAllConnected(token);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(FriendResponse::getFirstName).containsExactlyInAnyOrder("Anna", "Piotr");
    }

    @Test
    void shouldSendMessage() {
        User sender = getUser(1L, "John", "Doe");
        User receiver = getUser(2L, "Anna", "Nowak");
        MessageRequest request = new MessageRequest();
        request.setReceiverId(receiver.getId());
        request.setContent("Cześć!");

        when(jwtService.extractUsername(token)).thenReturn(sender.getEmail());
        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(messageRepository.save(any(Message.class))).thenAnswer(inv -> {
            Message m = inv.getArgument(0);
            m.setId(1L);
            m.setSendAt(LocalDateTime.now());
            return m;
        });

        MessageResponse response = messageService.sendMessage(token, request);

        assertThat(response.getContent()).isEqualTo("Cześć!");
        assertThat(response.isOwn()).isTrue();
        assertThat(response.getSendAt()).isNotNull();
    }

    @Test
    void shouldThrowWhenSenderNotFound() {
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        MessageRequest request = new MessageRequest();
        request.setReceiverId(2L);
        request.setContent("test");

        assertThatThrownBy(() -> messageService.sendMessage(token, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Sender not found");
    }

    @Test
    void shouldThrowWhenReceiverNotFound() {
        User sender = getUser(1L, "John", "Doe");
        when(jwtService.extractUsername(token)).thenReturn(sender.getEmail());
        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        MessageRequest request = new MessageRequest();
        request.setReceiverId(2L);
        request.setContent("test");

        assertThatThrownBy(() -> messageService.sendMessage(token, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Receiver not found");
    }

    @Test
    void shouldGetMessagesWithUser() {
        User currentUser = getUser(1L, "John", "Doe");
        User otherUser = getUser(2L, "Anna", "Nowak");

        Message m1 = Message.builder()
                .id(1L)
                .sender(currentUser)
                .receiver(otherUser)
                .content("Cześć")
                .sendAt(LocalDateTime.now().minusMinutes(5))
                .build();
        Message m2 = Message.builder()
                .id(2L)
                .sender(otherUser)
                .receiver(currentUser)
                .content("Hej")
                .sendAt(LocalDateTime.now())
                .build();

        when(jwtService.extractUsername(token)).thenReturn(currentUser.getEmail());
        when(userRepository.findByEmail(currentUser.getEmail())).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));
        when(messageRepository.findConversationBetween(currentUser, otherUser)).thenReturn(List.of(m1, m2));

        List<MessageResponse> responses = messageService.getMessagesWith(token, otherUser.getId());

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getContent()).isEqualTo("Cześć");
        assertThat(responses.get(0).isOwn()).isTrue();
        assertThat(responses.get(1).getContent()).isEqualTo("Hej");
        assertThat(responses.get(1).isOwn()).isFalse();
    }

    @Test
    void shouldThrowWhenGetMessagesWithUserNotFound() {
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.getMessagesWith(token, 2L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldThrowWhenGetMessagesWithOtherUserNotFound() {
        User currentUser = getUser(1L, "John", "Doe");
        when(jwtService.extractUsername(token)).thenReturn(currentUser.getEmail());
        when(userRepository.findByEmail(currentUser.getEmail())).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.getMessagesWith(token, 2L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Other user not found");
    }
}
