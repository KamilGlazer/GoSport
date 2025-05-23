package com.kamilglazer.gosport.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String headline;
    private String mobile;
    private String city;
    private String postalCode;
}
