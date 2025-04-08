package com.kamilglazer.gosport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSearch {
     private Long userId;
     private String firstName;
     private String lastName;
     private String profileImage;

     public String getProfileImage() {
          return profileImage !=null ? "http://localhost:8080/uploads/" + profileImage : null;
     }

}
