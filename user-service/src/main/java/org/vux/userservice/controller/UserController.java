package org.vux.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.vux.userservice.request.RegisterRequest;
import org.vux.userservice.response.CommonResponse;
import org.vux.userservice.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public CommonResponse createUser(@RequestBody RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

    @GetMapping("/{email}")
    public CommonResponse getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
}
