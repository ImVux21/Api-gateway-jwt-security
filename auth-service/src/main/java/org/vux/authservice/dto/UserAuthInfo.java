package org.vux.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthInfo {
    // this class is used to return the user details to AuthService for authentication,
    // so that it contains the password field
    private String id;
    private String email;
    private String password;
    private String role;
}
