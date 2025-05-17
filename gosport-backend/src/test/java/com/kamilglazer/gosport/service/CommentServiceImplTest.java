package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.request.CommentRequest;
import com.kamilglazer.gosport.dto.response.CommentResponse;
import com.kamilglazer.gosport.exception.IllegalActionException;
import com.kamilglazer.gosport.model.Comment;
import com.kamilglazer.gosport.model.Post;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.model.UserCredentials;
import com.kamilglazer.gosport.rabbit.CommentNotificationPayload;
import com.kamilglazer.gosport.rabbit.NotificationProducer;
import com.kamilglazer.gosport.repository.CommentRepository;
import com.kamilglazer.gosport.repository.PostRepository;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private NotificationProducer notificationProducer;
    private JwtService jwtService;
    private CommentServiceImpl commentService;

    private final String token = "test.token";
    private final String email = "john.doe@example.com";

    private User getUser(Long id, String firstName, String lastName) {
        UserCredentials credentials = new UserCredentials();
        credentials.setProfileImage("img.png");
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .credentials(credentials)
                .build();
    }

    private Post getPost(Long id, User user) {
        return Post.builder()
                .id(id)
                .user(user)
                .build();
    }

    private Comment getComment(Long id, User user, Post post, String content) {
        return Comment.builder()
                .id(id)
                .user(user)
                .post(post)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        userRepository = mock(UserRepository.class);
        postRepository = mock(PostRepository.class);
        notificationProducer = mock(NotificationProducer.class);
        jwtService = mock(JwtService.class);
        commentService = new CommentServiceImpl(commentRepository, userRepository, postRepository, jwtService, notificationProducer);
    }

    @Test
    void shouldAddCommentAndSendNotificationIfNotOwnPost() {
        User user = getUser(1L, "John", "Doe");
        User postOwner = getUser(2L, "Anna", "Nowak");
        Post post = getPost(10L, postOwner);
        CommentRequest request = new CommentRequest();
        request.setPostId(post.getId());
        request.setContent("Super post!");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> {
            Comment c = inv.getArgument(0);
            c.setId(100L);
            c.setCreatedAt(LocalDateTime.now());
            return c;
        });

        CommentResponse response = commentService.addComment(token, request);

        assertThat(response.getContent()).isEqualTo("Super post!");
        assertThat(response.getAuthorFirstName()).isEqualTo("John");
        verify(notificationProducer).sendNotification(any(CommentNotificationPayload.class));
    }

    @Test
    void shouldAddCommentWithoutNotificationIfOwnPost() {
        User user = getUser(1L, "John", "Doe");
        Post post = getPost(10L, user);
        CommentRequest request = new CommentRequest();
        request.setPostId(post.getId());
        request.setContent("Mój własny komentarz");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> {
            Comment c = inv.getArgument(0);
            c.setId(101L);
            c.setCreatedAt(LocalDateTime.now());
            return c;
        });

        CommentResponse response = commentService.addComment(token, request);

        assertThat(response.getContent()).isEqualTo("Mój własny komentarz");
        verify(notificationProducer, never()).sendNotification(any());
    }

    @Test
    void shouldThrowWhenUserNotFoundOnAddComment() {
        CommentRequest request = new CommentRequest();
        request.setPostId(10L);
        request.setContent("test");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.addComment(token, request))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldThrowWhenPostNotFoundOnAddComment() {
        User user = getUser(1L, "John", "Doe");
        CommentRequest request = new CommentRequest();
        request.setPostId(10L);
        request.setContent("test");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.addComment(token, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Post not found");
    }

    @Test
    void shouldReturnCommentsForPostSortedByCreatedAtDesc() {
        User user = getUser(1L, "John", "Doe");
        Post post = getPost(10L, user);
        Comment c1 = getComment(1L, user, post, "A");
        Comment c2 = getComment(2L, user, post, "B");

        when(commentRepository.findByPostIdOrderByCreatedAtDesc(post.getId()))
                .thenReturn(List.of(c2, c1));

        List<CommentResponse> responses = commentService.getCommentsForPost(post.getId());

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getContent()).isEqualTo("B");
        assertThat(responses.get(1).getContent()).isEqualTo("A");
    }

    @Test
    void shouldDeleteOwnComment() {
        User user = getUser(1L, "John", "Doe");
        Comment comment = getComment(1L, user, getPost(10L, user), "test");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(token, 1L);

        verify(commentRepository).delete(comment);
    }

    @Test
    void shouldThrowWhenDeletingCommentNotOwn() {
        User user = getUser(1L, "John", "Doe");
        User other = getUser(2L, "Anna", "Nowak");
        Comment comment = getComment(1L, other, getPost(10L, other), "test");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.deleteComment(token, 1L))
                .isInstanceOf(IllegalActionException.class)
                .hasMessage("You are not allowed to delete this comment");
    }

    @Test
    void shouldThrowWhenUserNotFoundOnDeleteComment() {
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(token, 1L))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldThrowWhenCommentNotFoundOnDelete() {
        User user = getUser(1L, "John", "Doe");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(token, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Comment not found");
    }
}
