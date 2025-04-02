package com.kamilglazer.gosport.exception.errorEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDetails {
    private String error;
    private String details;
    private LocalDateTime timestamp;
}
