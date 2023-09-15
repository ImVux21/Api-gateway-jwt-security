package org.vux.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vux.authservice.request.LoginRequest;
import org.vux.authservice.request.RegisterRequest;
import org.vux.authservice.response.CommonResponse;
import org.vux.authservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public CommonResponse register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public CommonResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
