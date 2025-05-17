package com.kamilglazer.gosport.controller;

import com.kamilglazer.gosport.config.JwtService;
import com.kamilglazer.gosport.dto.response.UserSearch;
import com.kamilglazer.gosport.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final JwtService jwtService;

    @Operation(
            summary = "Toggle trainer status",
            description = "Marks the current authenticated user as a trainer or removes the trainer status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer status updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/toggle")
    public ResponseEntity<Void> toggleTrainerStatus(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        trainerService.toggleTrainerStatus(token);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Check trainer status",
            description = "Returns whether the current authenticated user is marked as a trainer."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer status retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/status")
    public ResponseEntity<Boolean> getTrainerStatus(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getToken(authHeader);
        return ResponseEntity.ok(trainerService.isTrainer(token));
    }

    @Operation(
            summary = "Search trainers by city and/or postal code",
            description = "Returns a list of users marked as trainers filtered by optional city and postal code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matching trainers found"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserSearch>> searchTrainers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String postalCode
    ) {
        return ResponseEntity.ok(trainerService.search(city, postalCode));
    }
}