package com.kamilglazer.gosport.repository;

import com.kamilglazer.gosport.model.Post;
import com.kamilglazer.gosport.model.PostLike;
import com.kamilglazer.gosport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
