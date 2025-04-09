package com.kamilglazer.gosport.repository;

import com.kamilglazer.gosport.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    Optional<Connection> findBySenderIdAndReceiverId(Long requesterId, Long receiverId);
}
