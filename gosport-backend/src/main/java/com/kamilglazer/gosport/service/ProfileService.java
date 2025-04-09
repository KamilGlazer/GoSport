package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.response.UserDetailsResponse;
import com.kamilglazer.gosport.dto.response.UserProfileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    UserProfileResponse getUserProfile(Long id, String token);
    String uploadAvatar(String token, MultipartFile file);
    Resource getAvatar(String token);
    UserDetailsResponse updateUserProfile(String token, UserDetailsResponse userDetailsResponse);
    UserDetailsResponse getMyProfile(String token);
}
