package org.vux.authservice.service;

import org.vux.authservice.request.LoginRequest;
import org.vux.authservice.request.RegisterRequest;
import org.vux.authservice.response.CommonResponse;

public interface AuthService {
    CommonResponse register(RegisterRequest registerRequest);

    CommonResponse login(LoginRequest loginRequest);
}
