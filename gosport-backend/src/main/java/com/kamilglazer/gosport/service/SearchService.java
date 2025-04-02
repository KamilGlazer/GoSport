package com.kamilglazer.gosport.service;

import com.kamilglazer.gosport.dto.response.UserSearch;

import java.util.List;

public interface SearchService {

    List<UserSearch> findByName(String query);

}
