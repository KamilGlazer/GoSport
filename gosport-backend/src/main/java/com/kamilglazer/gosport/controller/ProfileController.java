package com.kamilglazer.gosport.controller;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.UserDetailsResponse;
import com.kamilglazer.gosport.service.ProfileService;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable Long id){
        return ResponseEntity.ok(profileService.getUserProfile(id));
    }

    @PutMapping
    public ResponseEntity<UserDetailsResponse> updateUserProfile(@RequestHeader("Authorization") String authHeader,@RequestBody UserDetailsResponse userDetailsResponse){
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(profileService.updateUserProfile(token,userDetailsResponse));
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestHeader("Authorization") String authHeader,@RequestParam("file") MultipartFile file){
        String token = jwtService.getToken(authHeader);
        String fileUrl = profileService.uploadAvatar(token,file);
        return ResponseEntity.ok(fileUrl);
    }

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
