package com.kamilglazer.gosport.controller;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.request.CommentRequest;
import com.kamilglazer.gosport.dto.response.CommentResponse;
import com.kamilglazer.gosport.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add a comment to a post", description = "Creates a new comment under a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CommentRequest request) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(commentService.addComment(token, request));
    }

    @Operation(summary = "Get comments for a post", description = "Retrieves all comments associated with a given post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId));
    }

    @Operation(summary = "Delete a comment", description = "Deletes a specific comment. Only the author of the comment can delete it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or not the comment owner"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Long commentId) {
        String token = jwtService.getToken(authHeader);
        commentService.deleteComment(token, commentId);
        return ResponseEntity.noContent().build();
    }
}