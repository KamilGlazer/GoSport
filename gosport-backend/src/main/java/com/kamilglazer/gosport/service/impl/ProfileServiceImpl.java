package com.kamilglazer.gosport.service.impl;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import com.kamilglazer.gosport.dto.response.UserDetailsResponse;
import com.kamilglazer.gosport.dto.response.UserProfileResponse;
import com.kamilglazer.gosport.exception.ProfileImageNotFoundException;
import com.kamilglazer.gosport.exception.UserNotFoundException;
import com.kamilglazer.gosport.model.Connection;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.model.UserCredentials;
import com.kamilglazer.gosport.repository.ConnectionRepository;
import com.kamilglazer.gosport.repository.UserCredentialsRepository;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final JwtService jwtService;
    private final ConnectionRepository connectionRepository;

    @Override
    public UserProfileResponse getUserProfile(Long id, String token) {
        String email = jwtService.extractUsername(token);
        User loggedUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        CONNECTION_STATUS connectionStatus = getConnectionStatusBetweenUsers(user.getId(), loggedUser.getId());

        return UserProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .city(user.getCredentials().getCity())
                .mobile(user.getCredentials().getMobile())
                .profileImage(user.getCredentials().getProfileImage())
                .headline(user.getCredentials().getHeadline())
                .connectionStatus(connectionStatus)
                .postalCode(user.getCredentials().getPostalCode())
                .build();
    }


    @Override
    public String uploadAvatar(String token, MultipartFile file) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        String fileName = fileStorageService.storeFile(file,user.getId());
        user.getCredentials().setProfileImage(fileName);
        userRepository.save(user);

        return "api/profile/avatar/" + user.getId();
    }

    @Override
    public Resource getAvatar(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if(user.getCredentials().getProfileImage() == null){
            throw new ProfileImageNotFoundException("Profile image not found");
        }

        return fileStorageService.loadFile(user.getCredentials().getProfileImage());
    }

    @Override
    public UserDetailsResponse updateUserProfile(String token, UserDetailsResponse userDetailsResponse) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserCredentials userCredentials = user.getCredentials();

        user.setFirstName(userDetailsResponse.getFirstName());
        user.setLastName(userDetailsResponse.getLastName());

        userCredentials.setCity(userDetailsResponse.getCity());
        userCredentials.setHeadline(userDetailsResponse.getHeadline());
        userCredentials.setPostalCode(userDetailsResponse.getPostalCode());
        userCredentials.setMobile(userDetailsResponse.getMobile());

        User savedUser = userRepository.save(user);

        return UserDetailsResponse.builder()
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .headline(savedUser.getCredentials().getHeadline())
                .mobile(savedUser.getCredentials().getMobile())
                .city(savedUser.getCredentials().getCity())
                .postalCode(savedUser.getCredentials().getPostalCode())
                .build();
    }

    @Override
    public UserDetailsResponse getMyProfile(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserCredentials userCredentials = user.getCredentials();

        return UserDetailsResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .city(userCredentials.getCity())
                .headline(userCredentials.getHeadline())
                .postalCode(userCredentials.getPostalCode())
                .mobile(userCredentials.getMobile())
                .build();
    }



    private CONNECTION_STATUS getConnectionStatusBetweenUsers(Long profileId, Long viewerId) {
        if (Objects.equals(profileId, viewerId)) {
            return null;
        }

        return connectionRepository
                .findBySenderIdAndReceiverId(profileId, viewerId)
                .or(() -> connectionRepository.findBySenderIdAndReceiverId(viewerId, profileId))
                .map(Connection::getStatus)
                .orElse(null);
    }
}
