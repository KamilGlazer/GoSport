package com.kamilglazer.gosport.service.impl;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.UserSearch;
import com.kamilglazer.gosport.exception.UserNotFoundException;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public List<UserSearch> findByName(String query,String token) {
        String email = jwtService.extractUsername(token);
        User loggedUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));


        if (query == null || query.isBlank()) {
            return List.of();
        }

        String[] parts = query.trim().split("\\s+");
        List<User> users;

        if (parts.length == 1) {
            String value = parts[0];
            users = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(value, value);
        } else {
            String firstName = parts[0];
            String lastName = parts[1];
            users = userRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
        }

        users.remove(loggedUser);

        return users.stream()
                .map(user -> new UserSearch(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getCredentials().getProfileImage()))
                .collect(Collectors.toList());
    }
}
