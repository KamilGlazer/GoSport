package com.kamilglazer.gosport.repository;

import com.kamilglazer.gosport.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
}
