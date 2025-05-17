package com.kamilglazer.gosport.repository;

import com.kamilglazer.gosport.dto.response.UserSearch;
import com.kamilglazer.gosport.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUserId(Long userId);
    List<Trainer> findAllByIsTrainerTrue();
}