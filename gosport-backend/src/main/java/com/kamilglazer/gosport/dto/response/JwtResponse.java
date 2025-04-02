package com.kamilglazer.gosport.dto.response;

import com.kamilglazer.gosport.domain.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String message;
    private USER_ROLE role;
}
