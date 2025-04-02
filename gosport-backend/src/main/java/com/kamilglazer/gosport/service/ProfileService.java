package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.response.UserDetailsResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    UserDetailsResponse getUserProfile(Long id);
    String uploadAvatar(String token, MultipartFile file);
    Resource getAvatar(String token);
    UserDetailsResponse updateUserProfile(String token, UserDetailsResponse userDetailsResponse);
}
