package com.kamilglazer.gosport.dto.response;


import com.kamilglazer.gosport.domain.CONNECTION_STATUS;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String headline;
    private String mobile;
    private String city;
    private String postalCode;
    private CONNECTION_STATUS connectionStatus;

    public String getProfileImage() {
        return profileImage !=null ? "http://localhost:8080/uploads/" + profileImage : null;
    }

}
