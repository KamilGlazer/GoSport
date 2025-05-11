package com.kamilglazer.gosport.service.impl;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.domain.NOTIFICATION_TYPE;
import com.kamilglazer.gosport.dto.request.CommentRequest;
import com.kamilglazer.gosport.dto.response.CommentResponse;
import com.kamilglazer.gosport.exception.IllegalActionException;
import com.kamilglazer.gosport.model.Comment;
import com.kamilglazer.gosport.model.Notification;
import com.kamilglazer.gosport.model.Post;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.repository.CommentRepository;
import com.kamilglazer.gosport.repository.NotificationRepository;
import com.kamilglazer.gosport.repository.PostRepository;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtService jwtService;
    private final NotificationRepository notificationRepository;

    @Override
    public CommentResponse addComment(String token, CommentRequest request) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();

        Comment saved = commentRepository.save(comment);

        if (!post.getUser().getId().equals(user.getId())) {
            Notification notification = Notification.builder()
                    .user(post.getUser())
                    .makerUser(user)
                    .type(NOTIFICATION_TYPE.POST_COMMENT)
                    .message(user.getFirstName() + " " + user.getLastName() + " commented on your post.")
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        }

        return mapToResponse(saved);
    }

    @Override
    public List<CommentResponse> getCommentsForPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorFirstName(comment.getUser().getFirstName())
                .authorLastName(comment.getUser().getLastName())
                .authorProfileImage(comment.getUser().getCredentials().getProfileImage())
                .authorId(comment.getUser().getId())
                .build();
    }

    @Override
    public void deleteComment(String token, Long commentId) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalActionException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
