package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.request.PostRequest;
import com.kamilglazer.gosport.dto.response.PostResponse;
import com.kamilglazer.gosport.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostRequest request) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(postService.createPost(token, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PostResponse>> getMyPosts(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(postService.getMyPosts(token));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeed(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(postService.getFeed(token));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Long postId) {
        String token = jwtService.getToken(authHeader);
        postService.deletePost(token, postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponse> toggleLike(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(postService.toggleLike(token, postId));
    }



}
