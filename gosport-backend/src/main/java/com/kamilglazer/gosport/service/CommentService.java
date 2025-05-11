package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.request.CommentRequest;
import com.kamilglazer.gosport.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(String token, CommentRequest request);
    List<CommentResponse> getCommentsForPost(Long postId);
    void deleteComment(String token, Long commentId);
}