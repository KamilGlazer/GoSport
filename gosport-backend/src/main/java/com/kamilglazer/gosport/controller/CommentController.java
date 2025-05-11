package com.kamilglazer.gosport.controller;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.request.CommentRequest;
import com.kamilglazer.gosport.dto.response.CommentResponse;
import com.kamilglazer.gosport.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CommentRequest request) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(commentService.addComment(token, request));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Long commentId) {
        String token = jwtService.getToken(authHeader);
        commentService.deleteComment(token, commentId);
        return ResponseEntity.noContent().build();
    }
}