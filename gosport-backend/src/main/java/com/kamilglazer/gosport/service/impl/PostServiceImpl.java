package com.kamilglazer.gosport.service.impl;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import com.kamilglazer.gosport.domain.NOTIFICATION_TYPE;
import com.kamilglazer.gosport.dto.request.PostRequest;
import com.kamilglazer.gosport.dto.response.PostResponse;
import com.kamilglazer.gosport.exception.IllegalActionException;
import com.kamilglazer.gosport.model.*;
import com.kamilglazer.gosport.repository.*;
import com.kamilglazer.gosport.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequestMapping
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;

    @Override
    public PostResponse createPost(String token, PostRequest request) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = Post.builder()
                .content(request.getContent())
                .user(user)
                .build();

        Post saved = postRepository.save(post);

        return PostResponse.builder()
                .id(saved.getId())
                .authorId(user.getId())
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt())
                .authorFirstName(user.getFirstName())
                .authorLastName(user.getLastName())
                .authorProfileImage(user.getCredentials().getProfileImage())
                .likeCount(0)
                .build();
    }

    @Override
    public List<PostResponse> getMyPosts(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getPosts().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(post -> mapToResponse(post, user))
                .toList();
    }

    @Override
    public List<PostResponse> getFeed(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<User> friends = Stream.concat(
                user.getSentConnections().stream()
                        .filter(c -> c.getStatus() == CONNECTION_STATUS.ACCEPTED)
                        .map(Connection::getReceiver),
                user.getReceivedConnections().stream()
                        .filter(c -> c.getStatus() == CONNECTION_STATUS.ACCEPTED)
                        .map(Connection::getSender)
        ).toList();

        List<Post> feedPosts = new ArrayList<>(user.getPosts());
        for (User friend : friends) {
            feedPosts.addAll(friend.getPosts());
        }

        return feedPosts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(post -> mapToResponse(post, user))
                .toList();
    }

    @Override
    @Transactional
    public void deletePost(String token, Long postId) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = postRepository.findById(postId).orElseThrow(() -> new UsernameNotFoundException("Post not found"));
        if(post.getUser().getId().equals(user.getId())) {
            commentRepository.deleteAllByPost(post);
            postLikeRepository.deleteAllByPost(post);
            postRepository.delete(post);
        }else{
            throw new IllegalActionException("You are not allowed to delete this post");
        }
    }

    @Override
    public PostResponse toggleLike(String token, Long postId) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Optional<PostLike> existing = postLikeRepository.findByUserAndPost(user, post);



        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get());
        } else {
            PostLike like = PostLike.builder()
                    .user(user)
                    .post(post)
                    .build();
            postLikeRepository.save(like);
            if(!user.getId().equals(post.getUser().getId())) {
                Notification notification = Notification.builder()
                        .user(post.getUser())
                        .makerUser(user)
                        .type(NOTIFICATION_TYPE.POST_LIKE)
                        .message(user.getFirstName() + " " + user.getLastName() + " liked your post.")
                        .isRead(false)
                        .build();
                notificationRepository.save(notification);
            }
        }

        Post refreshed = postRepository.findById(postId).orElseThrow();
        return mapToResponse(refreshed, user);
    }

    private PostResponse mapToResponse(Post post, User currentUser) {
        boolean likedByMe = post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(currentUser.getId()));

        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .authorFirstName(post.getUser().getFirstName())
                .authorLastName(post.getUser().getLastName())
                .authorProfileImage(post.getUser().getCredentials().getProfileImage())
                .likeCount(post.getLikes() != null ? post.getLikes().size() : 0)
                .authorId(post.getUser().getId())
                .likedByMe(likedByMe)
                .build();
    }
}
