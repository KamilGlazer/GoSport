package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.response.UserSearch;

import java.util.List;

public interface TrainerService {
    void toggleTrainerStatus(String token);
    boolean isTrainer(String token);
    List<UserSearch> search(String city, String postalCode);
}
