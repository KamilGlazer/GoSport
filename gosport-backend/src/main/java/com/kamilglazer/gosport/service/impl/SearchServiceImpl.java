package com.kamilglazer.gosport.service.impl;

import com.kamilglazer.gosport.dto.response.UserSearch;
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

    @Override
    public List<UserSearch> findByName(String query) {
        String firstName = "";
        String lastName = "";

        String[] parts = query.split(" ");
        if (parts.length > 0) {
            firstName = parts[0];
        }
        if (parts.length > 1) {
            lastName = parts[1];
        }
        List<User> users = userRepository.findByFirstNameContainingOrLastNameContaining(firstName,lastName);
        return users.stream().map(user -> new UserSearch(user.getId(),user.getFirstName(),user.getLastName(),user.getCredentials().getProfileImage())).collect(Collectors.toList());
    }
}
