package com.kamilglazer.gosport.controller;



import com.kamilglazer.gosport.dto.response.UserSearch;
import com.kamilglazer.gosport.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<UserSearch>> searchUser(@RequestParam String query){
        return ResponseEntity.ok(searchService.findByName(query));
    }

}
