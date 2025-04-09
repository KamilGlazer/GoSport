package com.kamilglazer.gosport.service.impl;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import com.kamilglazer.gosport.exception.ConnectionExistsException;
import com.kamilglazer.gosport.exception.ConnectionNotFoundException;
import com.kamilglazer.gosport.exception.IllegalConnectionAction;
import com.kamilglazer.gosport.exception.UserNotFoundException;
import com.kamilglazer.gosport.model.Connection;
import com.kamilglazer.gosport.model.User;
import com.kamilglazer.gosport.repository.ConnectionRepository;
import com.kamilglazer.gosport.repository.UserRepository;
import com.kamilglazer.gosport.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public Void createConnection(Long id, String token) {
        String email = jwtService.extractUsername(token);
        User loggedUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        if(id.equals(loggedUser.getId())) {
            throw new ConnectionExistsException("Connection already exists");
        }

        CONNECTION_STATUS connectionStatus = getConnectionStatusBetweenUsers(id,loggedUser.getId());
        if(connectionStatus != null) {
            throw new ConnectionExistsException("Connection already exists");
        }

        Connection connection = new Connection();
        connection.setSender(loggedUser);
        connection.setReceiver(user);
        connection.setStatus(CONNECTION_STATUS.PENDING);
        connectionRepository.save(connection);
        return null;
    }


    @Override
    public Void updateConnection(Long id, String token, String action) {
        String email = jwtService.extractUsername(token);
        User loggedUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        if(id.equals(loggedUser.getId())) {
            throw new ConnectionExistsException("Connection already exists");
        }

        Connection connection = connectionRepository
                .findBySenderIdAndReceiverId(loggedUser.getId(), id)
                .or(() -> connectionRepository.findBySenderIdAndReceiverId(id, loggedUser.getId()))
                .orElseThrow(() -> new ConnectionNotFoundException("Connection not found"));

        if ("DELETE".equalsIgnoreCase(action)) {
            connectionRepository.delete(connection);
        } else if ("ACCEPTED".equalsIgnoreCase(action)) {
            connection.setStatus(CONNECTION_STATUS.ACCEPTED);
            connectionRepository.save(connection);
        } else {
            throw new IllegalConnectionAction("Invalid action: " + action);
        }
        return null;
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
