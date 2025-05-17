package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.request.PostRequest;
import com.kamilglazer.gosport.dto.response.PostResponse;
import com.kamilglazer.gosport.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a post", description = "Creates a new post for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostRequest request) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(postService.createPost(token, request));
    }

    @Operation(summary = "Get my posts", description = "Returns all posts created by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/my")
    public ResponseEntity<List<PostResponse>> getMyPosts(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(postService.getMyPosts(token));
    }

    @Operation(summary = "Get feed posts", description = "Returns posts from the authenticated user and their friends.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feed retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeed(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(postService.getFeed(token));
    }

    @Operation(summary = "Delete a post", description = "Deletes a post created by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden (not the owner)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Long postId) {
        String token = jwtService.getToken(authHeader);
        postService.deletePost(token, postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Like or unlike a post", description = "Toggles like status on a post for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like toggled successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponse> toggleLike(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(postService.toggleLike(token, postId));
    }

}
