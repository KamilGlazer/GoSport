package com.kamilglazer.gosport.repository;

import com.kamilglazer.gosport.model.Comment;
import com.kamilglazer.gosport.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
    void deleteAllByPost(Post post);
}
