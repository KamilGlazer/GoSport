package com.kamilglazer.gosport.repository;

import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import com.kamilglazer.gosport.model.Connection;
import com.kamilglazer.gosport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    Optional<Connection> findBySenderIdAndReceiverId(Long requesterId, Long receiverId);
    List<Connection> findAllBySenderOrReceiverAndStatus(User sender, User receiver, CONNECTION_STATUS status);
}
