package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.UserDetailsResponse;
import com.kamilglazer.gosport.dto.response.UserProfileResponse;
import com.kamilglazer.gosport.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@CrossOrigin("*")
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;

    @Operation(summary = "Get public user profile", description = "Returns the public profile information of a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id, @RequestHeader("Authorization") String authHeader){
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(profileService.getUserProfile(id,token));
    }

    @Operation(summary = "Get my profile", description = "Returns the authenticated user's profile details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<UserDetailsResponse> getMyProfile(@RequestHeader("Authorization") String authHeader){
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(profileService.getMyProfile(token));
    }

    @Operation(summary = "Update my profile", description = "Updates the authenticated user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PutMapping
    public ResponseEntity<UserDetailsResponse> updateUserProfile(@RequestHeader("Authorization") String authHeader,@RequestBody UserDetailsResponse userDetailsResponse){
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(profileService.updateUserProfile(token,userDetailsResponse));
    }

    @Operation(summary = "Upload avatar", description = "Uploads a profile avatar image for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestHeader("Authorization") String authHeader,@RequestParam("file") MultipartFile file){
        String token = jwtService.getToken(authHeader);
        String fileUrl = profileService.uploadAvatar(token,file);
        return ResponseEntity.ok(fileUrl);
    }

    @Operation(summary = "Get my avatar", description = "Returns the authenticated user's avatar image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Avatar not found", content = @Content)
    })
    @GetMapping("/avatar")
    public ResponseEntity<Resource> getAvatar(@RequestHeader("Authorization") String authHeader){
        String token = jwtService.getToken(authHeader);
        Resource file = profileService.getAvatar(token);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
