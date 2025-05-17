package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import com.kamilglazer.gosport.dto.request.PostRequest;
import com.kamilglazer.gosport.dto.response.PostResponse;
import com.kamilglazer.gosport.exception.IllegalActionException;
import com.kamilglazer.gosport.model.*;
import com.kamilglazer.gosport.repository.*;
import com.kamilglazer.gosport.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    private PostRepository postRepository;
    private JwtService jwtService;
    private UserRepository userRepository;
    private PostLikeRepository postLikeRepository;
    private NotificationRepository notificationRepository;
    private CommentRepository commentRepository;
    private PostServiceImpl postService;

    private final String token = "test.token";
    private final String email = "john.doe@example.com";

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        jwtService = mock(JwtService.class);
        userRepository = mock(UserRepository.class);
        postLikeRepository = mock(PostLikeRepository.class);
        notificationRepository = mock(NotificationRepository.class);
        commentRepository = mock(CommentRepository.class);
        postService = new PostServiceImpl(postRepository, jwtService, userRepository, postLikeRepository, notificationRepository, commentRepository);
    }

    private User getUser(Long id, String firstName, String lastName) {
        UserCredentials credentials = new UserCredentials();
        credentials.setProfileImage("img.png");
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .credentials(credentials)
                .posts(new ArrayList<>())
                .sentConnections(new ArrayList<>())
                .receivedConnections(new ArrayList<>())
                .build();
    }

    @Test
    void shouldCreatePost() {
        User user = getUser(1L, "John", "Doe");
        PostRequest request = new PostRequest();
        request.setContent("Test post");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> {
            Post p = inv.getArgument(0);
            p.setId(10L);
            p.setCreatedAt(LocalDateTime.now());
            return p;
        });

        PostResponse response = postService.createPost(token, request);

        assertThat(response.getContent()).isEqualTo("Test post");
        assertThat(response.getAuthorFirstName()).isEqualTo("John");
        assertThat(response.getAuthorLastName()).isEqualTo("Doe");
        assertThat(response.getLikeCount()).isZero();
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void shouldGetMyPostsSorted() {
        User user = getUser(1L, "John", "Doe");
        Post post1 = Post.builder().id(1L).content("A").createdAt(LocalDateTime.now().minusDays(1)).user(user).likes(new ArrayList<>()).build();
        Post post2 = Post.builder().id(2L).content("B").createdAt(LocalDateTime.now()).user(user).likes(new ArrayList<>()).build();
        user.setPosts(List.of(post1, post2));

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        List<PostResponse> posts = postService.getMyPosts(token);

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getContent()).isEqualTo("B");
        assertThat(posts.get(1).getContent()).isEqualTo("A");
    }

    @Test
    void shouldGetFeedWithFriendsPosts() {
        User user = getUser(1L, "John", "Doe");
        User friend = getUser(2L, "Anna", "Nowak");

        Post myPost = Post.builder().id(1L).content("My post").createdAt(LocalDateTime.now()).user(user).likes(new ArrayList<>()).build();
        Post friendPost = Post.builder().id(2L).content("Friend post").createdAt(LocalDateTime.now().minusHours(1)).user(friend).likes(new ArrayList<>()).build();
        user.setPosts(List.of(myPost));
        friend.setPosts(List.of(friendPost));

        Connection connection = Connection.builder().status(CONNECTION_STATUS.ACCEPTED).receiver(friend).build();
        user.setSentConnections(List.of(connection));
        user.setReceivedConnections(new ArrayList<>());

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        List<PostResponse> feed = postService.getFeed(token);

        assertThat(feed).hasSize(2);
        assertThat(feed.get(0).getContent()).isEqualTo("My post");
        assertThat(feed.get(1).getContent()).isEqualTo("Friend post");
    }

    @Test
    void shouldDeleteOwnPost() {
        User user = getUser(1L, "John", "Doe");
        Post post = Post.builder().id(1L).user(user).build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(token, 1L);

        verify(commentRepository).deleteAllByPost(post);
        verify(postLikeRepository).deleteAllByPost(post);
        verify(postRepository).delete(post);
    }

    @Test
    void shouldThrowWhenDeletingNotOwnPost() {
        User user = getUser(1L, "John", "Doe");
        User other = getUser(2L, "Anna", "Nowak");
        Post post = Post.builder().id(1L).user(other).build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> postService.deletePost(token, 1L))
                .isInstanceOf(IllegalActionException.class)
                .hasMessage("You are not allowed to delete this post");
    }

    @Test
    void shouldToggleLikeAndCreateNotification() {
        User user = getUser(1L, "John", "Doe");
        User author = getUser(2L, "Anna", "Nowak");
        Post post = Post.builder().id(1L).user(author).likes(new ArrayList<>()).build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post)).thenReturn(Optional.of(post));
        when(postLikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());

        PostLike like = PostLike.builder().user(user).post(post).build();
        when(postLikeRepository.save(any(PostLike.class))).thenReturn(like);

        post.setLikes(List.of(like));

        PostResponse response = postService.toggleLike(token, 1L);

        verify(postLikeRepository).save(any(PostLike.class));
        verify(notificationRepository).save(any(Notification.class));
        assertThat(response.getLikeCount()).isEqualTo(1);
        assertThat(response.isLikedByMe()).isTrue();
    }

    @Test
    void shouldToggleLikeAndRemoveLike() {
        User user = getUser(1L, "John", "Doe");
        Post post = Post.builder().id(1L).user(user).likes(new ArrayList<>()).build();
        PostLike like = PostLike.builder().user(user).post(post).build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post)).thenReturn(Optional.of(post));
        when(postLikeRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(like));

        post.setLikes(new ArrayList<>());

        PostResponse response = postService.toggleLike(token, 1L);

        verify(postLikeRepository).delete(like);
        assertThat(response.getLikeCount()).isZero();
        assertThat(response.isLikedByMe()).isFalse();
    }
}
