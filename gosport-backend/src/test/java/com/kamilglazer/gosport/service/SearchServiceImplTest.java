package com.kamilglazer.gosport.service;


import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.UserSearch;
import com.kamilglazer.gosport.exception.UserNotFoundException;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.model.UserCredentials;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.impl.SearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceImplTest {

    private UserRepository userRepository;
    private JwtService jwtService;
    private SearchServiceImpl searchService;

    private final String token = "test.token";
    private final String email = "john.doe@example.com";

    private User getUser() {
        UserCredentials credentials = new UserCredentials();
        credentials.setProfileImage("img.png");
        return User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .credentials(credentials)
                .build();
    }

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);
        searchService = new SearchServiceImpl(userRepository, jwtService);
    }

    @Test
    void shouldReturnEmptyListWhenQueryIsNull() {
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(getUser()));

        List<UserSearch> result = searchService.findByName(null, token);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenQueryIsBlank() {
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(getUser()));

        List<UserSearch> result = searchService.findByName("   ", token);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> searchService.findByName("John", token))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }
}