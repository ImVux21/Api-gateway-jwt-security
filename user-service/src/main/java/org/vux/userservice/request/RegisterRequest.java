package org.vux.userservice.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
