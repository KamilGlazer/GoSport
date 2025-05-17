package com.kamilglazer.gosport.service.impl;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.UserSearch;
import com.kamilglazer.gosport.model.Trainer;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.repository.TrainerRepository;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.TrainerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void toggleTrainerStatus(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Trainer trainer = trainerRepository.findByUserId(user.getId())
                .orElse(Trainer.builder().user(user).isTrainer(false).build());

        trainer.setTrainer(!trainer.isTrainer());
        trainerRepository.save(trainer);
    }

    @Override
    public boolean isTrainer(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return trainerRepository.findByUserId(user.getId())
                .map(Trainer::isTrainer)
                .orElse(false);
    }

    @Override
    public List<UserSearch> search(String city, String postalCode) {
        List<Trainer> trainers = trainerRepository.findAllByIsTrainerTrue();

        return trainers.stream()
                .map(Trainer::getUser)
                .filter(user -> {
                    boolean matchCity = (city == null || city.isBlank()) ||
                            (user.getCredentials().getCity() != null &&
                                    user.getCredentials().getCity().toLowerCase().contains(city.toLowerCase()));
                    boolean matchPostal = (postalCode == null || postalCode.isBlank()) ||
                            (user.getCredentials().getPostalCode() != null &&
                                    user.getCredentials().getPostalCode().toLowerCase().contains(postalCode.toLowerCase()));
                    return matchCity && matchPostal;
                })
                .map(user -> UserSearch.builder()
                        .userId(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .profileImage(user.getCredentials().getProfileImage())
                        .build()
                ).toList();
    }
}
