package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.request.PostRequest;
import com.kamilglazer.gosport.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(String token, PostRequest request);
    List<PostResponse> getMyPosts(String token);
    List<PostResponse> getFeed(String token);
    void deletePost(String token, Long postId);
    PostResponse toggleLike(String token, Long postId);
}
