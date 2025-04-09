package com.kamilglazer.gosport.service;

public interface ConnectionService {
    Void createConnection(Long id, String token);

    Void updateConnection(Long id, String token, String action);
}
