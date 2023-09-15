package org.vux.authservice.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
